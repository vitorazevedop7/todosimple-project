import axios from "axios";

// CODE REVIEW [CONFIGURAÇÃO]: URL base hardcoded impede o funcionamento em ambientes diferentes.
// Em Docker, o backend é acessível como 'http://backend:8080', não 'localhost:8080'.
// O docker-compose.yml já define NEXT_PUBLIC_API_URL=http://backend:${SPRING_DOCKER_PORT},
// mas o código não consome essa variável.
// Sugestão: baseURL: process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080"
const api = axios.create({
  baseURL: "http://localhost:8080",
});

// CODE REVIEW [SEGURANÇA]: Token JWT lido do localStorage em toda requisição.
// localStorage é vulnerável a ataques XSS — qualquer script malicioso injetado na página
// pode roubar o token e se autenticar como o usuário.
// Sugestão: armazenar o JWT em cookie httpOnly + SameSite=Strict (inacessível via JavaScript).
// Isso requer ajuste no backend para enviar o token via Set-Cookie ao invés do header Authorization.
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers["Authorization"] = token;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default api;
