"use client";
import { useEffect, useState } from "react";

const greenVariants = [
  "bg-green-600",
  "bg-green-700",
  "bg-emerald-700",
  "bg-lime-700",
  "bg-green-800",
  "bg-emerald-800",
  "bg-lime-600",
];

export default function CheckTaskCube({ checked, onToggle }) {
  const [rotated, setRotated] = useState(checked);
  const [color, setColor] = useState("bg-green-700");

  useEffect(() => {
    const randomColor = greenVariants[Math.floor(Math.random() * greenVariants.length)];
    setColor(randomColor);
  }, []);

  useEffect(() => {
    setRotated(checked);
  }, [checked]);

  return (
    <div
      onClick={onToggle}
      className={`w-8 h-8 mr-3 rounded-md cursor-pointer transition-transform duration-700 transform relative flex items-center justify-center
        ${color}
        ${rotated ? "rotate-[180deg] scale-105 shadow-lg" : ""}
        opacity-100 scale-100 transition-all duration-500 ease-out`}
    >
      <span
        className={`text-white text-sm font-bold transition-opacity duration-500 transform
          ${rotated ? "opacity-100 rotate-[-180deg]" : "opacity-0"} 
          not-italic no-underline !line-through-none`}
        style={{ textDecoration: "none" }} // força sem line-through
      >
        ✓
      </span>
    </div>
  );
}
