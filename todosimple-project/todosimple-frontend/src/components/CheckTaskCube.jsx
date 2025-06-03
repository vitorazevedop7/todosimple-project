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
      className={`min-w-[2rem] min-h-[2rem] max-w-[2rem] max-h-[2rem]
        rounded-md cursor-pointer flex items-center justify-center
        transition-transform duration-700 transform
        ${color} ${rotated ? "rotate-[180deg] scale-105 shadow-lg" : ""}
        mr-2 sm:mr-3`}
    >
      <span
        className={`text-white text-sm font-bold transition-opacity duration-500 transform
          ${rotated ? "opacity-100 rotate-[-180deg]" : "opacity-0"}
          not-italic no-underline`}
        style={{ textDecoration: "none", fontStyle: "normal" }}
      >
        ✓
      </span>
    </div>
  );
}
