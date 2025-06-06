"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import api from "@/services/api";
import Image from "next/image";
import BlockCubeCheck from "@/components/BlockCubeCheck";

export default function RegisterPage() {
  const router = useRouter();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleRegister = async (e) => {
    e.preventDefault();
    if (password.length < 4) {
      alert("Password must contain at least 4 characters");
      return;
    }
    try {
      await api.post("/user", {
        username,
        password,
        user_profile: "1",
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
      <div className="w-full max-w-md text-white">
        {/* Cubos decorativos no topo */}
        <div className="flex justify-center mb-6 flex-wrap">
          {[...Array(5)].map((_, i) => (
            <BlockCubeCheck key={i} index={i} autoRotate />
          ))}
        </div>

        {/* Logo da aplicação */}
        <div className="flex justify-center mb-8">
          <Image
            src="/assets/logo.png"
            alt="ToDo.ai Logo"
            width={200}
            height={60}
            priority
          />
        </div>

        <div className="bg-white/5 border border-green-400 p-6 sm:p-8 rounded-xl shadow-lg">
          <h2 className="text-2xl font-bold mb-6 text-center">Create Account </h2>
          <form onSubmit={handleRegister} className="space-y-4">
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full px-4 py-3 rounded-lg bg-transparent border border-green-300 text-white placeholder-green-300 focus:outline-none focus:ring-2 focus:ring-green-500"
            />
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-4 py-3 rounded-lg bg-transparent border border-green-300 text-white placeholder-green-300 focus:outline-none focus:ring-2 focus:ring-green-500"
            />
            <button
              type="submit"
              className="w-full py-3 bg-green-400 text-white font-semibold rounded-lg hover:bg-green-300 transition"
            >
              Sign up
            </button>
          </form>
          <p
            className="mt-4 text-center text-sm text-green-200 cursor-pointer hover:underline"
            onClick={() => router.push("/login")}
          >
            Already have an account? Login here
          </p>
        </div>
      </div>
    </div>
  );
}
