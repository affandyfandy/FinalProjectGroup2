/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    extend: {},
  },
  plugins: [
    require('daisyui'),
  ],
  daisyui: {
    themes: [
      {
        mytheme: {
          "primary": "#ffffff",
          "primary-content": "#111827",
          "secondary": "#7a49fe",
          "secondary-content": "#f3f4f6",
          "accent": "#7a49fe",
          "accent-content": "#f3f4f6",
          "neutral": "#1e1d27",
          "neutral-content": "#f3f4f6",
          "base-100": "#080711",
          "base-200": "#444445",
          "base-300": "#1e1d27",
          "base-content": "#f3f4f6",
          "info": "#1d4ed8",
          "info-content": "#f3f4f6",
          "success": "#40be42",
          "success-content": "#111827",
          "warning": "#facc15",
          "warning-content": "#111827",
          "error": "#ef4444",
          "error-content": "#111827",
        },
      },
    ],
  },
}