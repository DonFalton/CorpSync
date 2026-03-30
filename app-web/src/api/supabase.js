import { createClient } from '@supabase/supabase-js'

// Obtenemos las variables de entorno de Vite
const supabaseUrl = import.meta.env.VITE_SUPABASE_URL
const supabaseAnonKey = import.meta.env.VITE_SUPABASE_ANON_KEY

// Creamos y exportamos el cliente para usarlo en toda la app
export const supabase = createClient(supabaseUrl, supabaseAnonKey)