import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { createClient } from "npm:@supabase/supabase-js@2";

// Definición estricta de interfaces
interface TriagePayload {
  ticket_id: number;
  titulo: string;
  descripcion: string;
}

interface GeminiTriageResponse {
  prioridad: 'baja' | 'media' | 'alta' | 'critica' | 'por_asignar';
  categoria: 'Hardware' | 'Software' | 'Redes' | 'Otros' | 'Sin_categorizar';
}

const SUPABASE_URL = Deno.env.get("SUPABASE_URL") as string;
const SUPABASE_SERVICE_ROLE_KEY = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY") as string;
const GEMINI_API_KEY = Deno.env.get("GEMINI_API_KEY") as string;
const WEBHOOK_SECRET = Deno.env.get("ITSM_WEBHOOK_SECRET") as string;

// Cliente Admin para saltar RLS (Solo instanciado una vez)
const supabaseAdmin = createClient(SUPABASE_URL, SUPABASE_SERVICE_ROLE_KEY);

Deno.serve(async (req: Request) => {
  // CORS Pre-flight
  if (req.method === 'OPTIONS') {
    return new Response('ok', { headers: { 'Access-Control-Allow-Origin': '*' } });
  }

  try {
    // 1. Validación de Seguridad (Webhook Signature)
    const signature = req.headers.get('x-corp-signature');
    if (!WEBHOOK_SECRET || signature !== WEBHOOK_SECRET) {
      console.error("Fallo de autenticación: Firma de webhook inválida.");
      return new Response("Unauthorized", { status: 401 });
    }

    const payload: TriagePayload = await req.json();

    if (!payload.ticket_id || (!payload.titulo && !payload.descripcion)) {
      return new Response("Bad Request: Campos faltantes", { status: 400 });
    }

    // 2. Llamada a Gemini 1.5 Flash usando Structured Outputs (JSON Schema estricto)
    const geminiUrl = `https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${GEMINI_API_KEY}`;

    const systemInstruction = `
      Eres el Agente de Triaje IT de CorpSync ITSM. Clasifica la incidencia técnica basándote en su título y descripción.
      Si el texto carece de sentido técnico, clasifícalo como "Sin_categorizar" y "por_asignar".
    `;

    const geminiRes = await fetch(geminiUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        system_instruction: { parts: [{ text: systemInstruction }] },
        contents: [{ parts: [{ text: `Título: ${payload.titulo}\nDescripción: ${payload.descripcion}` }] }],
        generationConfig: {
          temperature: 0.1, // Determinismo máximo
          response_mime_type: "application/json",
          response_schema: {
            type: "OBJECT",
            properties: {
              prioridad: {
                type: "STRING",
                enum: ["baja", "media", "alta", "critica", "por_asignar"]
              },
              categoria: {
                type: "STRING",
                enum: ["Hardware", "Software", "Redes", "Otros", "Sin_categorizar"]
              }
            },
            required: ["prioridad", "categoria"]
          }
        }
      })
    });

    if (!geminiRes.ok) {
      throw new Error(`Gemini API Error: ${await geminiRes.text()}`);
    }

    const geminiData = await geminiRes.json();
    const rawText = geminiData.candidates?.[0]?.content?.parts?.[0]?.text;
    if (!rawText) throw new Error("Respuesta vacía de Gemini");

    const triageResult: GeminiTriageResponse = JSON.parse(rawText);

    // 3. Persistencia en Supabase (con bloqueo por estado para evitar sobreescritura manual)
    const { data, error: updateError } = await supabaseAdmin
      .from("tickets")
      .update({
        prioridad: triageResult.prioridad,
        categoria: triageResult.categoria
      })
      .eq("id", payload.ticket_id)
      .eq("estado", "pendiente") // CRÍTICO: No sobreescribir si un humano ya lo atendió
      .select("id");

    if (updateError) throw updateError;

    if (!data || data.length === 0) {
      console.warn(`Ticket ${payload.ticket_id} no actualizado. Posiblemente ya modificado por un técnico.`);
    }

    return new Response(JSON.stringify({ success: true, triage: triageResult }), {
      status: 200,
      headers: { "Content-Type": "application/json" }
    });

  } catch (err: any) {
    console.error("Error crítico en Edge Function:", err.message);
    // Retornamos 500 para dejar traza en el dashboard de Supabase, 
    // pero el cliente JDBC/App no se entera porque la llamada original de pg_net es "fire-and-forget".
    return new Response(JSON.stringify({ error: "Internal Server Error" }), { status: 500 });
  }
});