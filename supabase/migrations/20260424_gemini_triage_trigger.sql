-- 1. Asegurar que la extensión asíncrona pg_net existe
CREATE EXTENSION IF NOT EXISTS pg_net;

-- 2. Función PL/pgSQL responsable de empaquetar el payload y disparar la llamada HTTP
CREATE OR REPLACE FUNCTION public.trigger_gemini_triage()
RETURNS trigger
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
  request_body jsonb;
  edge_function_url text := 'https://mofuozleqotjlsavxfkv.supabase.co/functions/v1/gemini-triage';
  -- Se asume que el token Service Role Key se aloja en current_setting o se hardcodea (no recomendado).
  -- Reemplazar <YOUR_SERVICE_ROLE_KEY> por tu clave real o usar Vault / pg_sodium.
  auth_token text := '<YOUR_SERVICE_ROLE_KEY>';
BEGIN
  -- Construimos el payload extrayendo únicamente los datos necesarios de la fila recién insertada
  request_body := jsonb_build_object(
    'ticket_id', NEW.id,
    'titulo', NEW.titulo,
    'descripcion', NEW.descripcion
  );

  -- Enviamos la petición POST asíncrona (esto enviará el trabajo al worker de pg_net inmediatamente 
  -- y dejará que la transacción principal haga un COMMIT sin bloqueos de red).
  PERFORM net.http_post(
    url := edge_function_url,
    body := request_body,
    headers := jsonb_build_object(
      'Content-Type', 'application/json',
      'Authorization', 'Bearer ' || auth_token
    )
  );

  RETURN NEW;
END;
$$;

-- 3. Crear el trigger acoplado a la tabla tickets
DROP TRIGGER IF EXISTS on_ticket_created_triage ON public.tickets;

CREATE TRIGGER on_ticket_created_triage
AFTER INSERT ON public.tickets
FOR EACH ROW
EXECUTE FUNCTION public.trigger_gemini_triage();
