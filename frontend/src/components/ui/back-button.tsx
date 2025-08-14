"use client"

import { Button } from "@/components/ui/button"
import { ArrowLeft } from "lucide-react"
import { useNavigate } from "react-router-dom"

interface BackButtonProps {
  href?: string
  label?: string
  variant?: "default" | "ghost" | "outline"
  size?: "default" | "sm" | "lg"
  className?: string
}

export function BackButton({ href, label = "Back", variant = "ghost", size = "sm", className }: BackButtonProps) {
  const navigate = useNavigate()

  const handleBack = () => {
    if (href) {
      navigate(href)
    } else {
      navigate(-1)
    }
  }

  return (
    <Button variant={variant} size={size} onClick={handleBack} className={`gap-2 ${className}`}>
      <ArrowLeft className="w-4 h-4" />
      {label}
    </Button>
  )
}
