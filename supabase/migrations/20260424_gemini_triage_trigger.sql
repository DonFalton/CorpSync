-- 1. Asegurar que la extensión asíncrona pg_net está habilitada
CREATE EXTENSION IF NOT EXISTS pg_net WITH SCHEMA extensions;

-- 2. Función PL/pgSQL para disparar el Webhook de forma segura y no bloqueante
CREATE OR REPLACE FUNCTION public.trigger_gemini_triage()
RETURNS trigger
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
  request_body jsonb;
  edge_function_url text := 'https://mofuozleqotjlsavxfkv.supabase.co/functions/v1/gemini-triage';
  webhook_secret text;
BEGIN
  -- Extraemos el secreto del webhook configurado de forma segura en Postgres
  -- Si no está configurado, usamos un fallback vacío para no romper la transacción
  webhook_secret := current_setting('app.settings.webhook_secret', true);

  -- Payload mínimo requerido por la IA
  request_body := jsonb_build_object(
    'ticket_id', NEW.id,
    'titulo', NEW.titulo,
    'descripcion', NEW.descripcion
  );

  -- net.http_post delega la llamada a un background worker (100% asíncrono)
  PERFORM net.http_post(
    url := edge_function_url,
    body := request_body,
    headers := jsonb_build_object(
      'Content-Type', 'application/json',
      'x-corp-signature', COALESCE(webhook_secret, '')
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