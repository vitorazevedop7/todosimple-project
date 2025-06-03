"use client";
import { useEffect, useState, useRef } from "react";
import { useRouter } from "next/navigation";
import { Trash2, PenSquare } from "lucide-react";
import Image from "next/image";
import api from "@/services/api";
import TaskFormModal from "@/components/TaskFormModal";
import BlockCubeCheck from "@/components/BlockCubeCheck";
import CheckTaskCube from "@/components/CheckTaskCube";

export default function DashboardPage() {
  const router = useRouter();
  const [username, setUsername] = useState("Usuário");
  const [tasks, setTasks] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editingTask, setEditingTask] = useState(null);
  const timers = useRef({});

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
      console.error("Erro ao buscar tarefas:", error);
    }
  };

  const fetchUser = async () => {
    try {
      const response = await api.get("/user/me");
      setUsername(response.data.username || "Usuário");
    } catch (error) {
      console.error("Erro ao buscar usuário:", error);
    }
  };

  const handleDelete = async (id) => {
    try {
      await api.delete(`/task/${id}`);
      fetchTasks();
    } catch (error) {
      console.error("Erro ao excluir tarefa:", error);
    }
  };

  const toggleDone = (task) => {
    const updatedTasks = tasks.map((t) =>
      t.id === task.id ? { ...t, done: !t.done } : t
    );
    setTasks(updatedTasks);

    if (!task.done) {
      timers.current[task.id] = setTimeout(() => {
        handleDelete(task.id);
        delete timers.current[task.id];
      }, 5 * 60 * 1000);
    } else {
      clearTimeout(timers.current[task.id]);
      delete timers.current[task.id];
    }
  };

  const getPossessiveTitle = (name) =>
    name.toLowerCase().endsWith("s") ? `${name}' Tasks` : `${name}'s Tasks`;

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#0f2414] to-[#1d5a2f] text-white px-4 py-6">
      <div className="w-full max-w-3xl mx-auto">
        {/* Header */}
        <header className="flex flex-col sm:flex-row sm:items-center justify-between gap-6 mb-10">
          <div className="flex items-center justify-center sm:justify-start gap-4 w-full sm:w-auto">
            <Image src="/assets/logo.png" alt="ToDo.ai Logo" width={160} height={40} priority />
            <BlockCubeCheck autoRotate />
          </div>
          <button
            onClick={() => setShowModal(true)}
            className="bg-green-300 text-green-900 px-6 py-2 rounded-xl font-semibold hover:bg-green-200 transition w-full sm:w-auto"
          >
            New Task +
          </button>
        </header>

        {/* Lista de Tarefas */}
        <section className="bg-white/10 border border-green-300 rounded-xl p-4 sm:p-6 shadow-lg">
          <h2 className="text-xl sm:text-2xl font-bold mb-6 text-white text-center sm:text-left">
            {getPossessiveTitle(username)}
          </h2>

          {tasks.length === 0 ? (
            <div className="border border-dashed border-green-400 p-6 rounded-lg text-center text-green-200">
              Nenhuma tarefa por enquanto.
            </div>
          ) : (
            <ul className="flex flex-col gap-4">
              {tasks.map((task) => (
                <li
                  key={task.id}
                  className="bg-white/5 border border-green-400 p-4 rounded-lg flex flex-wrap sm:flex-nowrap items-start justify-between gap-4 transition-all hover:scale-[1.015] hover:bg-white/10 hover:border-green-300"
                >
                  <div className="flex items-start gap-2 flex-1 min-w-0">
                    <CheckTaskCube
                      checked={task.done}
                      onToggle={() => toggleDone(task)}
                    />
                    <div
                      className="cursor-pointer break-words overflow-hidden"
                      onClick={() => setEditingTask(task)}
                    >
                      <h3
                        className={`text-base sm:text-lg font-semibold break-words max-w-full sm:max-w-md ${
                          task.done ? "italic line-through text-green-300" : ""
                        }`}
                      >
                        {task.description}
                      </h3>
                    </div>
                  </div>

                  <div className="flex items-center gap-2 shrink-0 ml-auto">
                    <button
                      onClick={() => setEditingTask(task)}
                      className="p-2 rounded-full text-green-200 hover:text-yellow-300 hover:bg-white/10 active:scale-90 transition-all"
                    >
                      <PenSquare size={20} />
                    </button>
                    <button
                      onClick={() => handleDelete(task.id)}
                      className="p-2 rounded-full text-green-200 hover:text-red-500 hover:bg-white/10 active:scale-90 transition-all"
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

      {/* Modal */}
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
