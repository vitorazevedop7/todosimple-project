"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Trash2, PenSquare } from "lucide-react";
import Image from "next/image";
import api from "@/services/api";
import TaskFormModal from "@/components/TaskFormModal";
import BlockCubeCheck from "@/components/BlockCubeCheck";

export default function DashboardPage() {
  const router = useRouter();
  const [username, setUsername] = useState("Usuário");
  const [tasks, setTasks] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editingTask, setEditingTask] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      router.push("/login");
    } else {
      fetchTasks();
      fetchUser();
    }
  }, [router]);

  const fetchTasks = async () => {
    try {
      const response = await api.get("/task/user");
      setTasks(response.data);
    } catch (error) {
      console.error("Erro ao buscar tarefas:", error.response || error.message || error);
      alert("Erro ao carregar tarefas.");
    }
  };

  const fetchUser = async () => {
    try {
      const response = await api.get("/user/me");
      setUsername(response.data.username || "Usuário");
    } catch (error) {
      console.error("Erro ao buscar usuário:", error.response || error.message || error);
    }
  };

  const handleDelete = async (id) => {
    const confirmar = confirm("Tem certeza que deseja excluir esta tarefa?");
    if (!confirmar) return;

    try {
      await api.delete(`/task/${id}`);
      fetchTasks();
    } catch (error) {
      console.error("Erro ao excluir tarefa:", error);
      alert("Erro ao excluir tarefa.");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#0f2414] to-[#1d5a2f] text-white px-4 py-6">
      <div className="max-w-4xl mx-auto w-full">
        {/* Header com logo e botão */}
        <header className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 mb-10">
          <div className="flex items-center gap-4">
            <Image
              src="/assets/logo.png"
              alt="ToDo.ai Logo"
              width={160}
              height={40}
              priority
            />
            <BlockCubeCheck autoRotate />
          </div>

          <button
            onClick={() => setShowModal(true)}
            className="bg-green-300 text-green-900 px-5 py-2 rounded-xl font-semibold hover:bg-green-200 transition"
          >
            Nova Tarefa +
          </button>
        </header>

        {/* Seção de tarefas com fundo estilizado */}
        <section className="bg-white/10 border border-green-300 rounded-xl p-6 shadow-lg">
          <h2 className="text-2xl font-bold mb-6 text-white">{username}'s Tasks</h2>

          {tasks.length === 0 ? (
            <div className="border border-dashed border-green-400 p-6 rounded-lg text-center text-green-200">
              Nenhuma tarefa por enquanto.
            </div>
          ) : (
            <ul className="grid gap-4">
              {tasks.map((task) => (
                <li
                  key={task.id}
                  className="bg-white/10 border border-green-400 p-4 rounded-lg flex items-center justify-between transition-all hover:scale-[1.015] hover:bg-white/20 hover:border-green-300"
                >
                  <div className="flex-1 cursor-pointer" onClick={() => setEditingTask(task)}>
                    <h3 className="text-lg font-semibold">{task.description}</h3>
                  </div>

                  <div className="flex items-center gap-2 ml-4">
                    <button
                      onClick={() => setEditingTask(task)}
                      className="p-2 rounded-full text-green-200 hover:text-yellow-300 hover:bg-white/10 active:scale-90 transition-all"
                      title="Editar tarefa"
                    >
                      <PenSquare size={20} />
                    </button>

                    <button
                      onClick={() => handleDelete(task.id)}
                      className="p-2 rounded-full text-green-200 hover:text-red-500 hover:bg-white/10 active:scale-90 transition-all"
                      title="Excluir tarefa"
                    >
                      <Trash2 size={20} />
                    </button>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </section>
      </div>

      {/* Modal de tarefa */}
      <TaskFormModal
        isOpen={showModal || editingTask !== null}
        onClose={() => {
          setShowModal(false);
          setEditingTask(null);
        }}
        onSuccess={() => {
          fetchTasks();
          setEditingTask(null);
        }}
        editingTask={editingTask}
      />
    </div>
  );
}
