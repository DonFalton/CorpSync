-- Habilitación de extensión de red para llamadas HTTP asíncronas desde Postgres
CREATE EXTENSION IF NOT EXISTS pg_net WITH SCHEMA extensions;

-- Invocación asíncrona de Edge Function aislada del hilo de transacción principal
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
  -- Extracción de firma criptográfica local para validación de origen en Deno
  webhook_secret := current_setting('app.settings.webhook_secret', true);

  -- Construcción de DTO mínimo para procesamiento de lenguaje natural
  request_body := jsonb_build_object(
    'ticket_id', NEW.id,
    'titulo', NEW.titulo,
    'descripcion', NEW.descripcion
  );

  -- Transmisión de carga útil vía Background Worker (Fire and Forget)
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

-- Acoplamiento de motor de IA al ciclo de vida de inserción de incidencias
DROP TRIGGER IF EXISTS on_ticket_created_triage ON public.tickets;

CREATE TRIGGER on_ticket_created_triage
AFTER INSERT ON public.tickets
FOR EACH ROW
EXECUTE FUNCTION public.trigger_gemini_triage();