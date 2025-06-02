"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import api from "@/services/api";
import Image from "next/image";

export default function RegisterPage() {
  const router = useRouter();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      await api.post("/user", {
        username,
        password,
        user_profile: "1" 
      });
      alert("Usuário criado com sucesso!");
      router.push("/login");
    } catch (error) {
      console.error("Erro ao criar usuário:", error);
      alert("Erro ao criar usuário.");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#0f2414] to-[#1d5a2f] flex items-center justify-center px-4">
      <div className="bg-white/5 border border-green-400 p-8 rounded-xl max-w-sm w-full text-white shadow-lg">
        <div className="flex justify-center mb-6">
          <Image src="/assets/logo.png" width={160} height={40} alt="Logo" priority />
        </div>
        <h2 className="text-2xl font-bold mb-6 text-center">Criar Conta</h2>
        <form onSubmit={handleRegister} className="space-y-4">
          <input
            type="text"
            placeholder="Usuário"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="w-full px-4 py-2 rounded-lg bg-transparent border border-green-300 text-white placeholder-green-300 focus:outline-none focus:ring-2 focus:ring-green-500"
          />
          <input
            type="password"
            placeholder="Senha"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full px-4 py-2 rounded-lg bg-transparent border border-green-300 text-white placeholder-green-300 focus:outline-none focus:ring-2 focus:ring-green-500"
          />
          <button
            type="submit"
            className="w-full bg-green-400 hover:bg-green-300 text-white py-2 rounded-lg font-semibold transition"
          >
            Cadastrar
          </button>
        </form>
        <p
          className="mt-4 text-center text-sm text-green-200 cursor-pointer hover:underline"
          onClick={() => router.push("/login")}
        >
          Alredy have an account? Login here
        </p>
      </div>
    </div>
  );
}
