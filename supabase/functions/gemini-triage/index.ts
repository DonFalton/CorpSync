import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { createClient } from "npm:@supabase/supabase-js@2";

interface TriagePayload {
  ticket_id: number;
  titulo: string;
  descripcion: string;
}

const SUPABASE_URL = Deno.env.get("SUPABASE_URL") as string;
const SUPABASE_SERVICE_ROLE_KEY = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY") as string;
const GEMINI_API_KEY = Deno.env.get("GEMINI_API_KEY") as string;
const WEBHOOK_SECRET = Deno.env.get("ITSM_WEBHOOK_SECRET") as string;

const supabaseAdmin = createClient(SUPABASE_URL, SUPABASE_SERVICE_ROLE_KEY);

Deno.serve(async (req: Request) => {
  console.info(`[LOG-INICIO] Petición recibida con método: ${req.method}`);

  if (req.method === 'OPTIONS') {
    return new Response('ok', { headers: { 'Access-Control-Allow-Origin': '*' } });
  }

  try {
    // 1. Verificar firma del Webhook
    const signature = req.headers.get('x-corp-signature');
    console.log(`[DEBUG-AUTH] Firma recibida en cabecera: "${signature}"`);
    console.log(`[DEBUG-AUTH] ¿Secreto del entorno configurado?: ${!!WEBHOOK_SECRET}`);

    if (!WEBHOOK_SECRET || signature !== WEBHOOK_SECRET) {
      console.error(`[ERROR-AUTH] Denegado. Firma coincidente: ${signature === WEBHOOK_SECRET}`);
      return new Response("Unauthorized", { status: 401 });
    }
    console.info("[LOG-AUTH] Autenticación de webhook correcta.");

    // 2. Leer Payload
    const payload: TriagePayload = await req.json();
    console.log("[DEBUG-PAYLOAD] Contenido del ticket a procesar:", JSON.stringify(payload));

    if (!payload.ticket_id || (!payload.titulo && !payload.descripcion)) {
      console.error("[ERROR-PAYLOAD] Payload malformado o campos vacíos.");
      return new Response("Bad Request", { status: 400 });
    }

    // 3. Conectar con Gemini
    console.info(`[LOG-AI] Solicitando clasificación a Gemini para el Ticket ID: ${payload.ticket_id}`);
    const geminiUrl = `https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=${GEMINI_API_KEY}`;

    const systemInstruction = "Eres el Agente de Triaje IT de CorpSync ITSM. Clasifica la incidencia basándote en su título y descripción.";

    console.log(`[DEBUG-AI] ¿API Key de Gemini configurada?: ${!!GEMINI_API_KEY}`);

    const geminiRes = await fetch(geminiUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        system_instruction: { parts: [{ text: systemInstruction }] },
        contents: [{ parts: [{ text: `Título: ${payload.titulo}\nDescripción: ${payload.descripcion}` }] }],
        generationConfig: {
          temperature: 0.1,
          response_mime_type: "application/json",
          response_schema: {
            type: "OBJECT",
            properties: {
              prioridad: { type: "STRING", enum: ["baja", "media", "alta", "critica", "por_asignar"] },
              categoria: { type: "STRING", enum: ["Hardware", "Software", "Redes", "Otros", "Sin_categorizar"] }
            },
            required: ["prioridad", "categoria"]
          }
        }
      })
    });

    if (!geminiRes.ok) {
      const errorTexto = await geminiRes.text();
      console.error(`[ERROR-AI] HTTP ${geminiRes.status} desde Gemini API. Detalles:`, errorTexto);
      throw new Error(`Gemini API falló con status ${geminiRes.status}`);
    }

    const geminiData = await geminiRes.json();
    const rawText = geminiData.candidates?.[0]?.content?.parts?.[0]?.text;
    console.log("[DEBUG-AI] Respuesta JSON cruda de Gemini:", rawText);

    if (!rawText) throw new Error("La estructura de respuesta de Gemini no contiene texto válido.");
    const triageResult = JSON.parse(rawText);

    // 4. Actualizar Base de Datos
    console.info(`[LOG-DB] Ejecutando UPDATE en base de datos para Ticket ID: ${payload.ticket_id}`);
    const { data, error: updateError } = await supabaseAdmin
      .from("tickets")
      .update({
        prioridad: triageResult.prioridad,
        categoria: triageResult.categoria
      })
      .eq("id", payload.ticket_id)
      .eq("estado", "pendiente")
      .select("id, estado, prioridad, categoria");

    if (updateError) {
      console.error("[ERROR-DB] Error al ejecutar el UPDATE en Supabase:", updateError.message);
      throw updateError;
    }

    console.log("[DEBUG-DB] Resultado del UPDATE (filas afectadas):", JSON.stringify(data));

    if (!data || data.length === 0) {
      console.warn(`[WARN-DB] No se actualizó ninguna fila. El ticket ${payload.ticket_id} podría no estar en estado 'pendiente'.`);
    } else {
      console.info(`[LOG-FIN] Proceso completado con éxito para Ticket ID: ${payload.ticket_id}`);
    }

    return new Response(JSON.stringify({ success: true, triage: triageResult }), {
      status: 200,
      headers: { "Content-Type": "application/json" }
    });

  } catch (err: any) {
    console.error("[ERROR-CRÍTICO] Excepción en la ejecución de la Edge Function:", err.message);
    return new Response(JSON.stringify({ error: err.message }), { status: 500 });
  }
});