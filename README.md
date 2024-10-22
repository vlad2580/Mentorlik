# MentorConnect - README

## Popis projektu

**MentorConnect** je webová aplikace určená k propojení uživatelů s mentory v oblasti IT. Uživatelé mohou vyhledávat mentory na základě jejich specializace, zkušeností a cenových preferencí, a mentoři mohou sdílet své znalosti s ostatními.

## Struktura projektu

Projekt je postaven na frameworku **Django** s využitím **PostgreSQL** jako databáze. Frontend používá HTML, CSS a JavaScript pro zajištění interaktivního a uživatelsky přívětivého rozhraní. Tento soubor slouží jako přehled hlavních částí projektu.

### 1. **Složka `/static`**
   - Obsahuje všechny statické soubory používané na webu, jako jsou styly CSS, obrázky a skripty JavaScriptu.

   - **Struktura:**
     - `/static/css/` - obsahuje soubory stylů CSS.
     - `/static/js/` - obsahuje JavaScriptové soubory.
     - `/static/images/` - obsahuje obrázky používané v aplikaci, například profilové obrázky mentorů a ikony.

### 2. **Složka `/templates`**
   - Obsahuje HTML šablony, které se používají ve spojení s Django backendem pro vykreslení stránek.

   - **Hlavní šablony:**
     - `index.html` - hlavní stránka s výpisem mentorů a sekcemi, jako je vyhledávání, filtrování, a informace o projektu.
     - `mentor_profile.html` - stránka s podrobnostmi o konkrétním mentorovi.

### 3. **Složka `/mentorconnect`**
   - Obsahuje všechny důležité soubory aplikace Django, jako jsou:
     - `models.py` - definuje databázové modely, včetně modelů pro mentory a uživatele.
     - `views.py`
