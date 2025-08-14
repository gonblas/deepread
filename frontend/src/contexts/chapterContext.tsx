"use client"

import type React from "react"
import { createContext, useContext, useState, useCallback } from "react"
import { useNotification } from "@/contexts/notificationContext"
import { useAuth } from "./authContext"
import Cookies from "js-cookie"

export interface Chapter {
  id: string
  title: string
  number: number
  summary: string
}

interface ChapterContextType {
  chapter: Chapter | null
  loading: boolean
  isSaving: boolean
  fetchChapter: (bookId: string, chapterId: string) => void
  createChapter: (bookId: string, data: Chapter) => Promise<Chapter>
  updateChapter: (bookId: string, chapterId: string, data: Chapter) => Promise<Chapter>
  setChapter: React.Dispatch<React.SetStateAction<Chapter | null>>
}

interface ErrorResponse {
  message: string
  statusCode: number
  details: string
}

const ChapterContext = createContext<ChapterContextType | undefined>(undefined)

export function ChapterProvider({ children }: { children: React.ReactNode }) {
  const { showSuccess, showError } = useNotification()
  const { logout } = useAuth()

  const [chapter, setChapter] = useState<Chapter | null>(null)
  const [loading, setLoading] = useState(false)
  const [isSaving, setIsSaving] = useState(false)

  const fetchChapter = useCallback(
    (bookId: string, chapterId: string) => {
      setLoading(true)
      fetch(`http://localhost:8080/api/books/${bookId}/chapters/${chapterId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${Cookies.get("token")}`,
        },
      })
        .then((response) => {
          if (!response.ok) {
            if (response.status === 401) {
              logout()
              return null
            }
            if (response.status === 404) {
              showError("Chapter not found")
              setChapter(null)
              return null
            }
            throw new Error(`Request failed with status ${response.status}`)
          }
          return response.json()
        })
        .then((data) => {
          setChapter(data)
        })
        .catch((error) => {
          showError("Failed to fetch chapter", error.message || "Unknown error")
          setChapter(null)
        })
        .finally(() => {
          setLoading(false)
        })
    },
    [logout, showError, showSuccess],
  )

  const createChapter = async (bookId: string, data: Chapter): Promise<Chapter> => {
    setIsSaving(true)

    try {
      const response = await fetch(`http://localhost:8080/api/books/${bookId}/chapters`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${Cookies.get("token")}`,
        },
        body: JSON.stringify(data),
      })

      if (!response.ok) {
        if (response.status === 401) {
          logout()
          throw new Error("Unauthorized")
        }
        const result = await response.json().catch(() => null)
        const message = result?.message || `Failed to create chapter: ${response.status} ${response.statusText}`
        throw new Error(message)
      }

      const result = await response.json()
      setChapter(result)
      showSuccess("Chapter created successfully", `Chapter "${result.title}" has been created.`)
      return result
    } catch (error) {
      showError("Failed to create chapter", error.message || "Unknown error")
      throw error
    } finally {
      setIsSaving(false)
    }
  }

  const updateChapter = async (bookId: string, chapterId: string, data: Chapter): Promise<Chapter> => {
    setIsSaving(true)
    try {
      const response = await fetch(`http://localhost:8080/api/books/${bookId}/chapters/${chapterId}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${Cookies.get("token")}`,
        },
        body: JSON.stringify(data),
      })

      if (!response.ok) {
        if (response.status === 401) {
          logout()
          throw new Error("Unauthorized")
        }
        const result = await response.json().catch(() => null)
        const message = result?.message || `Failed to update chapter: ${response.status} ${response.statusText}`
        throw new Error(message)
      }

      const result = await response.json()
      setChapter(result)
      showSuccess("Chapter updated successfully", `Chapter "${result.title}" has been updated.`)
      return result
    } catch (error) {
      showError("Failed to update chapter", error.message || "Unknown error")
      throw error
    } finally {
      setIsSaving(false)
    }
  }

  return (
    <ChapterContext.Provider
      value={{
        chapter,
        loading,
        isSaving,
        fetchChapter,
        createChapter,
        updateChapter,
        setChapter,
      }}
    >
      {children}
    </ChapterContext.Provider>
  )
}

export function useChapter() {
  const context = useContext(ChapterContext)
  if (!context) {
    throw new Error("useChapter must be used within a ChapterProvider")
  }
  return context
}
