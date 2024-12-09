/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      gradientColorStops: {
        'custom-green-start': '#4CAF50',
        'custom-green-end': '#A5D6A7',
        'custom-blue-start': '#2196F3',
        'custom-blue-end': '#90CAF9',
      },
    },
  },
  plugins: [],
}

