import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig(({ mode }) => ({
  base: "/admin/", // ✅ static/admin 폴더에 넣기 위함
  plugins: [react()],
  server: {
    host: '0.0.0.0',
    port: 5173,
    historyApiFallback: true,
    allowedHosts: ['kiosktest.shop'],
    ...(mode === 'development' && {
      proxy: {
        '/login': {
          target: 'https://kiosktest.shop:8081',
          changeOrigin: true,
          secure: false,
        },
        '/logout': {
          target: 'https://kiosktest.shop:8081',
          changeOrigin: true,
          secure: false,
        },
        '/api': {
          target: 'https://kiosktest.shop:8081',
          changeOrigin: true,
          secure: false,
        },
      },
    }),
  },
}));
