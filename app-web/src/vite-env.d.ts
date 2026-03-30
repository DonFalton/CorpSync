// src/vite-env.d.ts

/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_SUPABASE_URL: string;
  readonly VITE_SUPABASE_ANON_KEY: string;
  // Añadiremos más variables aquí en el futuro si es necesario
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}