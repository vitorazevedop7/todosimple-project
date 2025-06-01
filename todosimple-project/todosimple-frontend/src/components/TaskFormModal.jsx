"use client";
import { useEffect, useState } from "react";
import api from "@/services/api";

export default function TaskFormModal({ isOpen, onClose, onSuccess, editingTask }) {
  const [description, setDescription] = useState("");

  useEffect(() => {
    if (editingTask) {
      setDescription(editingTask.description);
    } else {
      setDescription("");
    }
  }, [editingTask]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const method = editingTask ? "put" : "post";
      const url = editingTask ? `/task/${editingTask.id}` : "/task";
      await api[method](url, { description });
      onSuccess();
      onClose();
    } catch (error) {
      console.error("Erro ao salvar tarefa:", error);
      alert("Erro ao salvar tarefa.");
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50">
      <div className="w-full max-w-md bg-white/5 backdrop-blur-lg text-white rounded-xl p-6 border border-green-400 shadow-2xl">
        <h2 className="text-2xl font-bold mb-4 text-center">
          {editingTask ? "Editar Tarefa" : "Nova Tarefa"}
        </h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="w-full px-4 py-3 bg-transparent border border-green-300 rounded-lg placeholder-green-300 text-white focus:outline-none focus:ring-2 focus:ring-green-500"
            placeholder="Descrição da tarefa"
          />
          <div className="flex justify-end gap-3">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 rounded-lg bg-white text-green-900 hover:bg-green-100 transition"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="px-4 py-2 rounded-lg bg-green-400 text-white font-semibold hover:bg-green-300 transition"
            >
              Salvar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
