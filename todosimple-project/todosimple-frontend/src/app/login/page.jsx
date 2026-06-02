"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import Image from "next/image";
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
        const cleanToken = token.replace("Bearer ", "");
        // CODE REVIEW [SEGURANÇA]: JWT armazenado em localStorage é vulnerável a XSS.
        // Qualquer script malicioso na página pode roubar o token via localStorage.getItem("token").
        // Sugestão: usar cookies httpOnly + SameSite=Strict, que são inacessíveis via JavaScript.
        localStorage.setItem("token", `Bearer ${cleanToken}`);
        router.push("/dashboard");
      } else {
        // CODE REVIEW [UX]: alert() nativo é bloqueante, não pode ser estilizado e
        // oferece péssima experiência, especialmente em mobile.
        // Sugestão: usar estado de erro no componente e renderizar inline, ou bibliotecas
        // como react-hot-toast ou sonner para notificações não-bloqueantes.
        alert("Token não encontrado na resposta.");
      }
    } catch (error) {
      console.error("Erro ao fazer login:", error);
      // CODE REVIEW [UX]: Mesmo problema de alert() acima. Substituir por feedback visual inline.
      alert("Usuário ou senha inválidos.");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#0f2414] to-[#1d5a2f] flex items-center justify-center px-4">
      <div className="w-full max-w-md text-white">
        {/* Cubos decorativos com rotação automática */}
        <div className="flex justify-center mb-6 flex-wrap">
          {[...Array(5)].map((_, i) => (
            <BlockCubeCheck key={i} index={i} autoRotate />
          ))}
        </div>

        {/* Logo */}
        <div className="flex justify-center mb-10">
          <Image
            src="/assets/logo.png"
            alt="ToDo.ai Logo"
            width={200}
            height={60}
            priority
          />
        </div>

        {/* Formulário de login */}
        <form className="space-y-6" onSubmit={handleLogin}>
          <div>
            <label className="block mb-1 text-sm">Username</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full px-4 py-3 bg-transparent border border-green-300 rounded-lg placeholder-green-300 text-white focus:outline-none focus:ring-2 focus:ring-green-500"
              placeholder="Enter your username"
            />
          </div>
          <div>
            <label className="block mb-1 text-sm">Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-4 py-3 bg-transparent border border-green-300 rounded-lg placeholder-green-300 text-white focus:outline-none focus:ring-2 focus:ring-green-500"
              placeholder="Enter your password"
            />
          </div>
          <button
            type="submit"
            className="w-full py-3 mt-4 bg-green-300 text-green-900 font-semibold rounded-lg hover:bg-green-200 transition"
          >
            Log in
          </button>
        </form>

        {/* Link para registro */}
        <div className="mt-6 flex justify-center">
          <p
            onClick={() => router.push("/register")}
            className="text-sm text-green-200 hover:underline cursor-pointer"
          >
            Create New Account
          </p>
        </div>
      </div>
    </div>
  );
}
