/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // 主色：低饱和石青
        primary: {
          50: '#f2f7f9',
          100: '#e2eff2',
          200: '#c5dfe5',
          300: '#a0c9d4',
          400: '#6eadbf',
          500: '#3B82A0',
          600: '#2F7D8C',
          700: '#256675',
          800: '#1f5462',
          900: '#1a4653',
        },
        // 语义色
        accent: {
          green:  '#34A853',
          amber:  '#F59E0B',
          red:    '#E05555',
          blue:   '#6B8EA4',
        },
        // 中性灰阶
        neutral: {
          50:  '#F7F8FA',
          100: '#F3F5F7',
          200: '#EEF0F3',
          300: '#E5E7EB',
          400: '#B6BEC9',
          500: '#8A94A6',
          600: '#6B7280',
          700: '#4B5563',
          800: '#374151',
          900: '#1F2933',
        },
      },
      borderRadius: {
        'sm': '4px',
        'md': '6px',
        'lg': '8px',
        'xl': '10px',
      },
      boxShadow: {
        'none': 'none',
        'sm': '0 1px 2px rgba(0, 0, 0, 0.04)',
        'soft': '0 1px 3px rgba(0, 0, 0, 0.06)',
        'medium': '0 4px 12px rgba(0, 0, 0, 0.08)',
        'card-hover': '0 2px 8px rgba(0, 0, 0, 0.08)',
      },
    },
  },
  plugins: [],
}
