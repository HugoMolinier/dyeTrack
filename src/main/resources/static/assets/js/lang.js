const langSwitcher = document.getElementById("languageSwitcher");

const savedLang = localStorage.getItem("lang") || "en";
const languageApp = urlParams.get('languageApp');

// si FR est choisi, on remplace le texte
if (savedLang === "fr"|| languageApp==="FR") {
    loadLanguage("fr");
}

if (langSwitcher) {
    langSwitcher.value = savedLang;

    langSwitcher.addEventListener("change", e => {
        const lang = e.target.value;
        if (lang === "fr") loadLanguage("fr");
        else location.reload(); // pour revenir au texte par défaut en anglais
        localStorage.setItem("lang", lang);
    });
}

async function loadLanguage(lang) {
    const res = await fetch(`assets/i18n/${lang}.json`);
    const translations = await res.json();

    document.querySelectorAll("[data-i18n]").forEach(el => {
        const key = el.dataset.i18n;
        const value = key.split('.').reduce((o, i) => o ? o[i] : null, translations);
        if (value) el.textContent = value;
    });
}
