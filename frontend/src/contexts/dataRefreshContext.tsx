"use client"

import React, { createContext, useContext, useCallback, useRef } from "react"
import { useNavigate } from "react-router-dom"
import { useNotification } from "@/contexts/notificationContext"

type ResourceType = "book" | "chapter" | "quiz" | "user" | "general"

type ResourceActionFn = (
  resourceType: ResourceType,
  actionFn: () => Promise<any>,
  successMessage?: string,
  redirectPath?: string
) => Promise<void>

interface DataRefreshContextType {
  refreshData: (resourceType?: ResourceType) => void
  createResource: ResourceActionFn
  updateResource: ResourceActionFn
  deleteResource: ResourceActionFn
}

const DataRefreshContext = createContext<DataRefreshContextType | undefined>(undefined)

export function DataRefreshProvider({ children }: { children: React.ReactNode }) {
  const navigate = useNavigate()
  const { showSuccess, showError, showLoading, dismissToast } = useNotification()
  const refreshCallbacks = useRef<Map<ResourceType | "general", (() => void)[]>>(new Map())

  const registerRefreshCallback = useCallback(
    (resourceType: ResourceType | "general", callback: () => void) => {
      const callbacks = refreshCallbacks.current.get(resourceType) || []
      callbacks.push(callback)
      refreshCallbacks.current.set(resourceType, callbacks)

      return () => {
        const currentCallbacks = refreshCallbacks.current.get(resourceType) || []
        const filteredCallbacks = currentCallbacks.filter((cb) => cb !== callback)
        refreshCallbacks.current.set(resourceType, filteredCallbacks)
      }
    },
    []
  )

  const refreshData = useCallback((resourceType?: ResourceType) => {
    if (resourceType) {
      const callbacks = refreshCallbacks.current.get(resourceType) || []
      callbacks.forEach((callback) => callback())
    } else {
      refreshCallbacks.current.forEach((callbacks) => {
        callbacks.forEach((callback) => callback())
      })
    }

    const generalCallbacks = refreshCallbacks.current.get("general") || []
    generalCallbacks.forEach((callback) => callback())
  }, [])

  const handleResourceOperation = useCallback(
    async (
      operationLabel: string,
      resourceType: ResourceType,
      actionFn: () => Promise<any>,
      defaultSuccessMessage: string,
      successMessage?: string,
      redirectPath?: string
    ) => {
      const loadingToast = showLoading(`${operationLabel} resource...`)

      try {
        await actionFn()
        dismissToast(loadingToast)
        refreshData(resourceType)
        setTimeout(() => {
          showSuccess(successMessage || defaultSuccessMessage)
        }, 100)

        if (redirectPath) {
          setTimeout(() => {
            navigate(redirectPath)
          }, 200)
        }
      } catch (error) {
        dismissToast(loadingToast)
        showError(
          `Failed to ${operationLabel.toLowerCase()} resource`,
          error instanceof Error ? error.message : "Unknown error occurred"
        )
        throw error
      }
    },
    [showLoading, showSuccess, showError, dismissToast, refreshData, navigate]
  )

  const createResource: ResourceActionFn = useCallback(
    (resourceType, fn, successMessage, redirectPath) =>
      handleResourceOperation(
        "Creating",
        resourceType,
        fn,
        "Resource created successfully",
        successMessage,
        redirectPath
      ),
    [handleResourceOperation]
  )

  const updateResource: ResourceActionFn = useCallback(
    (resourceType, fn, successMessage, redirectPath) =>
      handleResourceOperation(
        "Updating",
        resourceType,
        fn,
        "Resource updated successfully",
        successMessage,
        redirectPath
      ),
    [handleResourceOperation]
  )

  const deleteResource: ResourceActionFn = useCallback(
    (resourceType, fn, successMessage, redirectPath) =>
      handleResourceOperation(
        "Deleting",
        resourceType,
        fn,
        "Resource deleted successfully",
        successMessage,
        redirectPath
      ),
    [handleResourceOperation]
  )

  const value: DataRefreshContextType = {
    refreshData,
    createResource,
    updateResource,
    deleteResource,
  }

  ;(value as any).registerRefreshCallback = registerRefreshCallback

  return <DataRefreshContext.Provider value={value}>{children}</DataRefreshContext.Provider>
}

export function useDataRefresh() {
  const context = useContext(DataRefreshContext)
  if (context === undefined) {
    throw new Error("useDataRefresh must be used within a DataRefreshProvider")
  }
  return context
}

export function useRefreshCallback(resourceType: ResourceType | "general", callback: () => void) {
  const context = useContext(DataRefreshContext)
  if (context === undefined) {
    throw new Error("useRefreshCallback must be used within a DataRefreshProvider")
  }

  const registerRefreshCallback = (context as any).registerRefreshCallback

  React.useEffect(() => {
    const cleanup = registerRefreshCallback(resourceType, callback)
    return cleanup
  }, [registerRefreshCallback, resourceType, callback])
}
