# Kiki — A CLI Task Manager

Kiki is a simple, testable, and extensible **command-line task manager** written in Java.  
It supports **Todos / Deadlines / Events**, case-insensitive **Find**, date filtering via **`on <date>`**, and automatic **persistence**—all structured with clean OOP (Parser → Command → Model/Storage/UI) and full Javadoc.

> **User Guide:** See [`docs/README.md`](docs/README.md).  
> **GitHub Pages (after enabled):** `https://kevin88866.github.io/ip/`

---

## Table of Contents
- [Features](#features)
- [Project Structure](#project-structure)
- [Quick Start](#quick-start)
    - [Prerequisites](#prerequisites)
    - [Build & Run (Linux/macOS)](#build--run-linuxmacos)
    - [Build & Run (Windows PowerShell)](#build--run-windows-powershell)
- [Usage Cheat Sheet](#usage-cheat-sheet)
- [Save File Format](#save-file-format)
- [Architecture Overview](#architecture-overview)
- [Error Handling](#error-handling)
- [Add a New Command](#add-a-new-command)
- [Troubleshooting](#troubleshooting)

---

## Features
- **Todos / Deadlines / Events**  
  Dates in input are strictly `yyyy-mm-dd` (ISO).
- **List / Mark / Unmark / Delete**
- **Find** by keyword
- **`on <yyyy-mm-dd>`** shows deadlines on that date and events whose date range **contains** that date
- **Persistent storage** at `data/kiki.txt`
- **Helpful error messages** for invalid inputs
- **A-MoreOOP** design: one class per command; clear separation of concerns
- **A-JavaDoc**: public classes/methods documented

---

## Project Structure
```
src/
 └─ kiki/
    ├─ Kiki.java                # app entry & main loop
    ├─ parser/Parser.java       # text -> Command
    ├─ command/                 # one class per command
    │   ├─ ListCommand.java
    │   ├─ AddTodoCommand.java
    │   ├─ AddDeadlineCommand.java
    │   ├─ AddEventCommand.java
    │   ├─ MarkCommand.java
    │   ├─ UnmarkCommand.java
    │   ├─ DeleteCommand.java
    │   ├─ FindCommand.java
    │   ├─ OnDateCommand.java
    │   └─ ExitCommand.java
    ├─ task/                    # model
    │   ├─ Task.java
    │   ├─ Todo.java
    │   ├─ Deadline.java
    │   ├─ Event.java
    │   └─ TaskList.java
    ├─ storage/Storage.java     # load/save data/kiki.txt
    ├─ ui/Ui.java               # all console I/O
    ├─ exception/KikiException.java
    └─ time/Dates.java          # parse/format dates
docs/
 └─ README.md                   # User Guide (GitHub Pages)
data/
 └─ kiki.txt                    # created on first run
```

---

## Quick Start

### Prerequisites
- Java **17+** (Java 11+ may work if your environment matches)
- A terminal (macOS/Linux/Windows PowerShell/WSL)
- 
#### Option 1: Run the Prebuilt JAR (Recommended for Users)
Download the latest release from [GitHub Releases](../../releases) and run:

```bash
java -jar kiki.jar
```

#### Option 2: Run the code

#### Build & Run (Linux/macOS)

```bash
# from project root
find src -name "*.java" > sources.txt
mkdir -p out
javac -d out @sources.txt

# run
java -cp out kiki.Kiki
```

#### Build & Run (Windows PowerShell)
```powershell
# from project root
Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName } | Set-Content sources.txt
mkdir out -ErrorAction SilentlyContinue
javac -d out @sources.txt

# run
java -cp out kiki.Kiki
```

> On first launch, `data/kiki.txt` is created automatically.

---

## Usage Cheat Sheet
```
list
todo <description>
deadline <description> /by <yyyy-mm-dd>
event <description> /from <yyyy-mm-dd> /to <yyyy-mm-dd>
mark <taskNumber>
unmark <taskNumber>
delete <taskNumber>
find <keyword>
on <yyyy-mm-dd>
bye
```
- Dates in **input** must be `yyyy-mm-dd`.
- Task numbers are **1-based** (use `list` to check).
- `find` matches **substrings**, case-insensitive.

---

## Save File Format
File: `data/kiki.txt` (one task per line)

**Todo**
```
T | <done:0|1> | <description>
```

**Deadline**
```
D | <done:0|1> | <description> | <yyyy-mm-dd>
```

**Event**
```
E | <done:0|1> | <description> | <from:yyyy-mm-dd> | <to:yyyy-mm-dd>
```

**Example**
```
T | 0 | read book
D | 1 | return book | 2019-10-15
E | 0 | project meeting | 2019-10-10 | 2019-10-12
```

---

## Architecture Overview
- **Kiki** — wires `Ui`, `Storage`, `TaskList`; main loop.
- **Parser** — converts raw text to a `Command`.
- **command** — one class per command; each implements `execute(TaskList, Ui, Storage)`.
- **task** — model types `Task`, `Todo`, `Deadline`, `Event`, plus `TaskList` collection logic.
- **storage.Storage** — serialization to/from `data/kiki.txt`.
- **ui.Ui** — console I/O.
- **time.Dates** — date parsing (`yyyy-mm-dd`) and friendly formatting (`MMM d yyyy`).
- **exception.KikiException** — user-facing failures.

This separation keeps parsing/commands independent of UI/storage and makes adding features straightforward.

---

## Error Handling
- Unknown command → friendly “I don’t know what that means”.
- Bad/missing indices → descriptive messages.
- Date format errors → “Please use date format `yyyy-mm-dd`”.
- Corrupt save file → warning, app continues with an empty list.

---

## Add a New Command
1. Create `kiki/command/MyCommand.java` extending `Command`.
2. Implement `execute(TaskList tasks, Ui ui, Storage storage)`.
3. Teach `Parser` to return your command for the new syntax.
4. If the command mutates tasks, call `storage.save(tasks)` inside `execute`.
5. Add Javadoc and minimal tests.

---

## Troubleshooting
- **Unknown command** → Check spelling (e.g., `deadline`, not `dateline`).
- **Date errors** → Input must be `yyyy-mm-dd`, e.g., `2019-10-15`.
- **“Task number is out of range”** → Run `list` to see valid 1-based indices.
- **File errors** → Ensure the process can read/write `data/kiki.txt`.
