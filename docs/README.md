# Kiki — User Guide

Kiki is a **command-line task manager** for tracking simple todos, deadlines, and events directly from your terminal.  
This page shows how to install, run, and use Kiki’s features step by step.

If you are a developer, see the project overview in the root [Readme.md](https://github.com/Kevin88866/ip/blob/A-Release/README.md)

---

## Contents
- [What You Can Do](#what-you-can-do)
- [Quick Start](#quick-start)
    - [Prerequisites](#prerequisites)
    - [Build & Run](#build--run)
- [Commands](#commands)
    - [Cheat Sheet](#cheat-sheet)
    - [Examples](#examples)
- [Save File Format](#save-file-format)
- [Tips](#tips)
- [Troubleshooting](#troubleshooting)
- [FAQ](#faq)
- [About](#about)

---

## What You Can Do
- Create **Todos / Deadlines / Events**
    - Dates in input are **strictly** `yyyy-mm-dd` (ISO).
- **List** tasks and manage them with **Mark / Unmark / Delete**.
- **Find** tasks by keyword (case-insensitive substring).
- Show items **on** a specific date:
    - Deadlines due **on** that date
    - Events whose date **range contains** that date
- Auto **save & load** from `data/kiki.txt`.

---

## Quick Start

### Prerequisites
- Java **17+** (Java 11+ may work if your environment matches)
- A terminal (macOS/Linux/Windows PowerShell/WSL)

#### Option 1: Run the Prebuilt JAR (Recommended for Users)
Download the latest release from [GitHub Releases](https://github.com/Kevin88866/ip/releases/tag/A-Release) and run:

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

## Commands

### Cheat Sheet
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

**Notes**
- Dates in **commands** must be `yyyy-mm-dd` (e.g., `2019-10-15`).
- Task numbers are **1-based** (use `list` to view them).
- `find` matches **substrings**, case-insensitive.

### Examples
```
____________________________________________________________
 Hello! I'm Kiki
  What can I do for you?
____________________________________________________________
todo read book
____________________________________________________________
 Got it. I've added this task:
    [T][ ] read book
  Now you have 1 tasks in the list.
____________________________________________________________
deadline return book /by 2019-10-15
____________________________________________________________
 Got it. I've added this task:
    [D][ ] return book (by: Oct 15 2019)
  Now you have 2 tasks in the list.
____________________________________________________________
event project meeting /from 2019-10-10 /to 2019-10-12
____________________________________________________________
 Got it. I've added this task:
    [E][ ] project meeting (from: Oct 10 2019 to: Oct 12 2019)
  Now you have 3 tasks in the list.
____________________________________________________________
find book
____________________________________________________________
  1. [T][ ] read book
  2. [D][ ] return book (by: Oct 15 2019)
____________________________________________________________
on 2019-10-10
____________________________________________________________
  3. [E][ ] project meeting (from: Oct 10 2019 to: Oct 12 2019)
____________________________________________________________
bye
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

---

## Save File Format

Location: `data/kiki.txt` (one task per line)

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

## Tips
- Prefer short, actionable descriptions (e.g., `todo cs2103 quiz`).
- Use `find` to quickly filter by keywords (e.g., `find book`).
- `on <date>` is great for day planning (shows matching deadlines and events).

---

## Troubleshooting
- **“Unknown command”** → Check spelling (e.g., `deadline`, not `dateline`).
- **Date format error** → Use `yyyy-mm-dd`, e.g., `2019-10-15`.
- **“Task number is out of range”** → Run `list` and use the shown 1-based index.
- **File errors** → Ensure the app can create/read/write `data/kiki.txt` under your working directory.

---

## FAQ
**Q: Which date format should I type?**  
A: Always `yyyy-mm-dd` (e.g., `2025-09-30`).

**Q: Are event dates inclusive?**  
A: Yes. `on 2019-10-10` will show an event from `2019-10-10` **to** `2019-10-12`.

**Q: Where is my data stored?**  
A: In `data/kiki.txt` inside your project folder.

**Q: Can I add times (hh:mm)?**  
A: Not in this minimal version; the app uses dates only.

---

## About
- **Name:** Kiki
- **Interface:** CLI
- **Storage:** Plain text (`data/kiki.txt`)
- **Target users:** Anyone who wants a lightweight task list in the terminal
