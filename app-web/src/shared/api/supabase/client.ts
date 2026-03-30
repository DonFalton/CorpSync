// src/shared/api/supabase/client.ts
import { createClient } from '@supabase/supabase-js';
import { ENV } from '../../../app/config/env';
import type { Database } from './types'; // Importación de tipos generados

// Instancia singleton fuertemente tipada
export const supabase = createClient<Database>(
  ENV.SUPABASE_URL,
  ENV.SUPABASE_ANON_KEY
);