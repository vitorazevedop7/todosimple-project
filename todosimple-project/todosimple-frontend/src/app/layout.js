import "./globals.css";
import { Inter } from "next/font/google";

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
  title: "ToDoSimple",
  description: "Sistema de tarefas com login JWT",
};

export default function RootLayout({ children }) {
  return (
    <html lang="pt-br">
      <body className={`${inter.className} bg-[#121212] text-white`}>
        {children}
      </body>
    </html>
  );
}
