import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { createClient } from "npm:@supabase/supabase-js@2";

// Definimos los tipos esperados
interface TriagePayload {
  ticket_id: number;
  titulo: string;
  descripcion: string;
}

interface GeminiResponse {
  prioridad: string;
  categoria: string;
}

const supabaseUrl = Deno.env.get("SUPABASE_URL") as string;
const supabaseServiceKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY") as string;
const geminiApiKey = Deno.env.get("GEMINI_API_KEY") as string;

Deno.serve(async (req: Request) => {
  // Manejo de pre-flight CORS
  if (req.method === 'OPTIONS') {
    return new Response('ok', { headers: { 'Access-Control-Allow-Origin': '*' } })
  }

  try {
    const payload: TriagePayload = await req.json();
    
    // Validación de la entrada
    if (!payload.ticket_id || !payload.titulo || !payload.descripcion) {
      return new Response(JSON.stringify({ error: "Faltan campos obligatorios" }), { 
        status: 400, 
        headers: { "Content-Type": "application/json" } 
      });
    }

    // 1. Invocación a Gemini API usando el modelo Gemini 1.5 Flash (veloz y económico)
    const geminiUrl = `https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${geminiApiKey}`;
    
    // Structured Prompt para obligar al modelo a devolver exclusivamente un JSON sin markdown fences
    const prompt = `
      Eres un asistente experto en triaje de ITSM de Nivel 1.
      Tu tarea es analizar el título y la descripción del ticket entrante y clasificarlo en un objeto JSON estricto.
      
      Reglas estrictas de salida:
      - "prioridad" debe ser exactamente uno de los siguientes valores en minúsculas: "baja", "media", "alta", "critica".
      - "categoria" debe inferirse en base al contenido.
      - Solo debes responder con el objeto JSON puro sin formato Markdown extra ni explicaciones.
      
      Ejemplo de salida:
      {"prioridad": "alta", "categoria": "hardware"}

      Ticket a clasificar:
      Título: ${payload.titulo}
      Descripción: ${payload.descripcion}
    `;

    const geminiRes = await fetch(geminiUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        contents: [{ parts: [{ text: prompt }] }],
        generationConfig: {
          response_mime_type: "application/json" // Feature de Gemini para forzar la salida estructurada
        }
      })
    });

    if (!geminiRes.ok) {
      throw new Error(`Gemini API error HTTP ${geminiRes.status}: ${await geminiRes.text()}`);
    }

    const geminiData = await geminiRes.json();
    const triageResultString = geminiData.candidates?.[0]?.content?.parts?.[0]?.text;
    
    if (!triageResultString) {
      throw new Error("Respuesta vacía o formato inesperado desde Gemini");
    }

    const triageResult: GeminiResponse = JSON.parse(triageResultString);

    // 2. Validación y limpieza (Fallback safety)
    const prioridadesValidas = ['baja', 'media', 'alta', 'critica'];
    const prioridadLimpia = prioridadesValidas.includes(triageResult.prioridad.toLowerCase()) 
      ? triageResult.prioridad.toLowerCase() 
      : 'por_asignar';

    const categoriaLimpia = triageResult.categoria ? triageResult.categoria.toLowerCase() : 'sin_categorizar';

    // 3. Modificación asíncrona en Supabase (Bypass RLS usando Service Role)
    const supabaseAdmin = createClient(supabaseUrl, supabaseServiceKey);
    
    const { error: updateError } = await supabaseAdmin
      .from("tickets")
      .update({ 
        prioridad: prioridadLimpia, 
        categoria: categoriaLimpia 
      })
      .eq("id", payload.ticket_id);

    if (updateError) {
      throw new Error(`Supabase Update Error: ${updateError.message}`);
    }

    return new Response(JSON.stringify({ success: true, triage: { prioridad: prioridadLimpia, categoria: categoriaLimpia } }), {
      headers: { "Content-Type": "application/json" },
      status: 200,
    });

  } catch (err: any) {
    console.error("Error en Edge Function gemini-triage:", err.message);
    // Retornar 500 silenciosamente para dejar traza en Edge Logs sin romper el cliente (ya que es asíncrono)
    return new Response(JSON.stringify({ error: 'Internal Server Error durante triaje asíncrono' }), { 
      status: 500,
      headers: { "Content-Type": "application/json" }
    });
  }
});
