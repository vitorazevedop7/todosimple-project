"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import api from "@/services/api";
import TaskFormModal from "@/components/TaskFormModal";

export default function DashboardPage() {
  const router = useRouter();
  const [username, setUsername] = useState("Usuário");
  const [tasks, setTasks] = useState([]);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      router.push("/login");
    } else {
      fetchTasks();
    }
  }, [router]);

  const fetchTasks = async () => {
    try {
      const response = await api.get("/task/user");
      setTasks(response.data);
    } catch (error) {
      console.error("Erro ao buscar tarefas:", error.response || error.message || error);
      alert("Erro ao carregar tarefas. Verifique se você está autenticado.");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#0f2414] to-[#1d5a2f] text-white px-4 py-6">
      <div className="max-w-4xl mx-auto w-full">
        <header className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 mb-10">
          <h1 className="text-3xl sm:text-4xl font-bold">Bem-vindo, {username} 👋</h1>
          <button
            onClick={() => setShowModal(true)}
            className="bg-green-300 text-green-900 px-5 py-2 rounded-xl font-semibold hover:bg-green-200 transition"
          >
            Nova Tarefa +
          </button>
        </header>

        <section>
          <h2 className="text-xl font-semibold mb-4">Suas Tarefas</h2>

          {tasks.length === 0 ? (
            <div className="border border-dashed border-green-400 p-6 rounded-lg text-center text-green-200">
              Nenhuma tarefa por enquanto.
            </div>
          ) : (
            <ul className="grid gap-4">
              {tasks.map((task) => (
                <li key={task.id} className="bg-white/10 border border-green-400 p-4 rounded-lg">
                  <h3 className="text-lg font-semibold">{task.description}</h3>
                  <p className="text-sm text-green-200">ID: {task.id}</p>
                </li>
              ))}
            </ul>
          )}
        </section>
      </div>

      {/* Modal de nova tarefa */}
      <TaskFormModal
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        onSuccess={fetchTasks}
      />
    </div>
  );
}
