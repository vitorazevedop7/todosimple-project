"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import api from "@/services/api";
import BlockCubeCheck from "@/components/BlockCubeCheck";

export default function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const router = useRouter();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post("/login", { username, password });
      const token = response.headers["authorization"];
      if (token) {
        localStorage.setItem("token", token);
        router.push("/dashboard");
      } else {
        alert("Token não encontrado na resposta.");
      }
    } catch (error) {
      console.error("Erro ao fazer login:", error);
      alert("Usuário ou senha inválidos.");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#0f2414] to-[#1d5a2f] flex items-center justify-center px-4">
      <div className="w-full max-w-md text-white">
        {/* Cubos com animação sequencial */}
        <div className="flex justify-center mb-6 flex-wrap">
          {[...Array(5)].map((_, i) => (
            <BlockCubeCheck key={i} index={i} />
          ))}
        </div>

        <h1 className="text-4xl font-bold text-center mb-10">ToDo</h1>

        <form className="space-y-6" onSubmit={handleLogin}>
          <div>
            <label className="block mb-1 text-sm">Username</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full px-4 py-3 bg-transparent border border-green-300 rounded-lg placeholder-green-300 text-white focus:outline-none focus:ring-2 focus:ring-green-500"
              placeholder="Digite seu usuário"
            />
          </div>
          <div>
            <label className="block mb-1 text-sm">Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-4 py-3 bg-transparent border border-green-300 rounded-lg placeholder-green-300 text-white focus:outline-none focus:ring-2 focus:ring-green-500"
              placeholder="Digite sua senha"
            />
          </div>
          <button
            type="submit"
            className="w-full py-3 mt-4 bg-green-300 text-green-900 font-semibold rounded-lg hover:bg-green-200 transition"
          >
            Log in
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-green-200 hover:underline cursor-pointer">
          Forgot password?
        </p>
      </div>
    </div>
  );
}
