# 📝 MultiNote  

A simple and clean Android notes app built with **Kotlin**, **Room Database**, and **MVVM architecture**.  
Create, edit, auto-save, and organize your notes with ease.  

---

## 🚀 Features (so far)

- **Create Notes** → Add a title and content with instant auto-save.  
- **Auto-Save** → Notes are saved automatically as you type (no save button needed).  
- **Edit Notes** → Tap a note card to open and edit it.  
- **Delete Notes** → Swipe right on a note card to delete it (with **Undo** via Snackbar).  
- **RecyclerView Grid** → Notes displayed in a neat 2-column grid layout.  
- **Empty State** → Shows a message when no notes exist.  

---

## 📸 Screenshots  
*will add soon...*

- Home Screen with Notes Grid  
- Add / Edit Note Screen  
- Swipe-to-Delete with Undo Snackbar  

---

## 🛠️ Tech Stack

- **Language:** Kotlin  
- **UI:** Material Design, RecyclerView, CardView  
- **Architecture:** MVVM (Model-View-ViewModel)  
- **Database:** Room Persistence Library  
- **Other:** LiveData, ViewModel, Coroutines  

---

## 📂 Project Structure
│
├── MainActivity.kt # Displays notes in RecyclerView
├── AddNoteActivity.kt # Create/Edit notes with auto-save
│
├── adapter/
│ └── NoteAdapter.kt # RecyclerView adapter for notes
│
├── room/
│ ├── Note.kt # Data class (Entity)
│ ├── NoteDao.kt # Database operations (DAO)
│ └── NoteDatabase.kt # Room database
│
├── viewmodel/
│ └── NoteViewModel.kt # Handles data between UI & DB


---

## ⚡ How It Works

- **Room** manages persistent storage.  
- **LiveData + ViewModel** keep UI in sync with database changes.  
- **Coroutines** handle background operations.  
- **AutoSave**: A debounce mechanism ensures efficient updates while typing.  

---

## 📌 Next Steps / Roadmap

- Add Navigation Drawer (Home, Archive, Settings).  
- Implement Note Categories or Tags.  
- Dark Mode support.  
- Cloud Sync (future scope).  

---

## 🤝 Contributing

Pull requests are welcome.  
For major changes, please open an issue first to discuss what you’d like to change.  

---

## 📄 License
this project solely belongs to me till no one else contributes but anyone who wants to use the app can download the app from App Release section
Pankaj Singh Rawat







