/** @type {import('tailwindcss').Config} */
export default {
  darkMode: "class",
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      fontFamily: {
        sans: ["Montserrat", "Kanit"],
        serif: ["Noto Serif Display Variable", "serif"],
        title: ["Noto Serif Display", "serif"],
      },
      screens: {
        xs: "480px",
        ss: "620px",
        sm: "870px",
        md: "1060px",
        lg: "1200px",
        xl: "1700px",
      },
      colors: {
        
      },
    },
  },
  plugins: [],
}
