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
     - `views.py` - obsahuje logiku, která řídí zpracování požadavků a vykreslování stránek.
     - `urls.py` - definuje URL trasy pro aplikaci.
     - `forms.py` - definuje formuláře používané na stránkách, například pro registraci mentorů.

### 4. **Složka `/media`**
   - Obsahuje nahrané soubory, které mohou uživatelé nebo mentoři poskytnout, například profilové fotografie.

### 5. **Databáze**
   - **PostgreSQL** je použita jako hlavní databáze pro ukládání informací o mentorech, uživatelích a rezervacích konzultací.
   - Pro připojení k databázi jsou přihlašovací údaje uloženy v souboru `settings.py`.

### 6. **Soubor `settings.py`**
   - Tento soubor obsahuje základní nastavení Django projektu, včetně konfigurace databáze, statických souborů a médií.
   - Zde jsou také definována různá nastavení pro vývoj a produkční prostředí.

### 7. **JavaScriptové funkce**
   - JavaScriptové funkce jsou definovány v souboru `/static/js/scripts.js`, které řídí dynamické prvky na stránce, například filtrování mentorů nebo zpracování vyhledávání.

## Pravidla

1. **Komentáře v kódu**:
   - Pro lepší spolupráci a srozumitelnost projektu by měly být všechny komentáře v kódu psány výhradně **v anglickém jazyce**.
   - To umožní, aby všichni členové týmu, včetně případných mezinárodních spolupracovníků, mohli snadno rozumět a pracovat s kódem.

2. **Větve pro různé části projektu**:
   - Pro usnadnění vývoje a správu verzí by měl být každému směru vývoje (frontend, backend, databáze atd.) přidělen samostatný branch.
   - Příklad větví:
     - `frontend` – pro vývoj a změny na uživatelském rozhraní (HTML, CSS, JavaScript).
     - `backend` – pro změny týkající se serverové logiky, databázových interakcí, nebo API.
     - `devops` – pro infrastrukturu, nasazování a konfiguraci CI/CD.
   - Změny by měly být začleněny do hlavní větve prostřednictvím **pull requestů**, po důkladném peer review.

## Spuštění projektu

### Lokální prostředí

1. **Klonování repozitáře:**
   ```bash
   git clone https://github.com/uzivatel/mentorconnect.git
   cd mentorconnect
