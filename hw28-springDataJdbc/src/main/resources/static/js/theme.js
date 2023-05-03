var currTheme

(() => {
    'use strict'

    const storedTheme = localStorage.getItem('theme')

    const getPreferredTheme = () => {
        if (storedTheme) {
            return storedTheme
        }

        return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
    }

    const setTheme = function (theme) {
        if (theme === 'auto' && window.matchMedia('(prefers-color-scheme: dark)').matches) {
            document.documentElement.setAttribute('data-bs-theme', 'dark')
            currTheme = 'dark'
        } else {
            document.documentElement.setAttribute('data-bs-theme', theme)
            currTheme = theme
        }
    }

    setTheme(getPreferredTheme())

    window.onload = () => {
        if (currTheme == 'dark') {
            let obj1 = document.querySelector(".btn-outline-dark");
            obj1.classList.remove('btn-outline-dark');
            obj1.classList.add('btn-outline-light');
            obj1 = document.querySelector(".btn-dark");
            obj1.classList.remove('btn-dark');
            obj1.classList.add('btn-light');
            obj1 = document.querySelector(".table-light");
            if (obj1 != null) {
                obj1.classList.remove('table-light');
                obj1.classList.add('table-dark');
            }
        }
    }

})()