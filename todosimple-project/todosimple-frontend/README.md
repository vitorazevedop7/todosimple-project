# ToDoSimple – Frontend 🖥️

This is the frontend of the ToDoSimple application, a minimalist and responsive interface built with **Next.js** and **Tailwind CSS**. It consumes a secure RESTful API and allows users to register, log in, and manage their tasks.

## ✨ Features

- ✅ Login and authentication via JWT  
- ✅ Task creation, update, and deletion  
- ✅ Check/uncheck task completion with animation  
- ✅ Auto-delete completed tasks after 5 minutes  
- ✅ Responsive design for mobile and desktop  
- ✅ Visual feedback and modern UI

## 🚀 Technologies

- React (via Next.js)  
- Tailwind CSS  
- Axios  
- LocalStorage (JWT token)  
- Vercel (deployment)

## 📂 Folder Structure
todosimple-frontend/
├── app/ # Next.js app directory
├── components/ # UI components (e.g., TaskBlock, LoginPage)
├── services/ # API configuration and requests (api.js)
├── public/ # Static files
├── styles/ # Global styles (if any)
└── README.md


## ⚙️ How to Run Locally

```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Open your browser at:
http://localhost:3000

Make sure the backend server is running and accessible at the correct URL defined in api.js.

Technical Notes
Uses useState and useRouter for managing auth and navigation.

Stores JWT securely in localStorage and includes it in API headers.

Handles success/error cases with basic alerts (can be improved with modals or toasts).

Goal Alignment
This frontend was designed with a professional, minimal and user-friendly look to showcase readiness for remote frontend or full-stack roles in international teams.