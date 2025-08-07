import React, { createContext, useContext, useState, useEffect } from 'react'
import axios from '@/lib/axios.config'

interface User {
  id: string
  email: string
  username: string
  token: string
}

interface AuthContextType {
  user: User | null
  login: (email: string, password: string) => Promise<boolean>
  signup: (email: string, password: string, username: string) => Promise<boolean>
  logout: () => void
  isLoading: boolean
}


const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const [token, setToken] = useState<string | null>(null)

  useEffect(() => {
    const savedUser = localStorage.getItem('user')
    if (savedUser) {
      setUser(JSON.parse(savedUser))
    }
    setIsLoading(false)
  }, [])

  const login = async (email: string, password: string): Promise<boolean> => {
    setIsLoading(true)
  
    setIsLoading(false)
    return true
  }

  const signup = async (email: string, password: string, username: string): Promise<boolean> => {
    setIsLoading(true)
    
    axios.post('/auth/signup', { email, password, username })
      .then((response: { data: { token: React.SetStateAction<string | null> } }) => {
        setToken(response.data.token)
      }
      )
      .catch((error: any) => {
        throw new Error(`Signup failed: ${error.message}`)
      }
    )

    setIsLoading(false)
    return true
  }

  const logout = () => {
    setUser(null)
    setToken(null)
    localStorage.removeItem('token')
  }

  return (
    <AuthContext.Provider value={{ user, login, signup, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}
