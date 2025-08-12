"use client"

import type React from "react"
import { createContext, useContext } from "react"
import { toast } from "sonner"

interface NotificationContextType {
  showSuccess: (message: string, description?: string) => void
  showError: (message: string, description?: string) => void
  showInfo: (message: string, description?: string) => void
  showWarning: (message: string, description?: string) => void
  showLoading: (message: string) => string | number
  dismissToast: (toastId: string | number) => void
}

const NotificationContext = createContext<NotificationContextType | undefined>(undefined)

export function NotificationProvider({ children }: { children: React.ReactNode }) {
  const showSuccess = (message: string, description?: string) => {
    toast.success(message, {
      description,
      duration: 4000,
    })
  }

  const showError = (message: string, description?: string) => {
    toast.error(message, {
      description,
      duration: 5000,
    })
  }

  const showInfo = (message: string, description?: string) => {
    toast.info(message, {
      description,
      duration: 4000,
    })
  }

  const showWarning = (message: string, description?: string) => {
    toast.warning(message, {
      description,
      duration: 4000,
    })
  }

  const showLoading = (message: string) => {
    return toast.loading(message)
  }

  const dismissToast = (toastId: string | number) => {
    toast.dismiss(toastId)
  }

  const value: NotificationContextType = {
    showSuccess,
    showError,
    showInfo,
    showWarning,
    showLoading,
    dismissToast,
  }

  return <NotificationContext.Provider value={value}>{children}</NotificationContext.Provider>
}

export function useNotification() {
  const context = useContext(NotificationContext)
  if (context === undefined) {
    throw new Error("useNotification must be used within a NotificationProvider")
  }
  return context
}
