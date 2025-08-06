import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { ThemeProvider } from "@/contexts/themeContext"
import { AuthProvider, useAuth } from '@/contexts/authContext'
import LoginPage from '@/pages/LoginPage'
import { Loader2 } from 'lucide-react'

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { user, isLoading } = useAuth()

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-ctp-base">
        <div className="text-center space-y-4">
          <Loader2 className="h-8 w-8 animate-spin text-ctp-mauve mx-auto" />
          <p className="text-ctp-subtext1">Cargando...</p>
        </div>
      </div>
    )
  }

  return user ? <>{children}</> : <Navigate to="/login" replace />
}

function PublicRoute({ children }: { children: React.ReactNode }) {
  const { user, isLoading } = useAuth()

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-ctp-base">
        <div className="text-center space-y-4">
          <Loader2 className="h-8 w-8 animate-spin text-ctp-mauve mx-auto" />
          <p className="text-ctp-subtext1">Cargando...</p>
        </div>
      </div>
    )
  }

  return user ? <Navigate to="/dashboard" replace /> : <>{children}</>
}

function AppRoutes() {
  return (
    <Routes>
      <Route 
        path="/login" 
        element={
          <PublicRoute>
            <LoginPage />
          </PublicRoute>
        } 
      />
      <Route 
        path="/dashboard" 
        element={
          <ProtectedRoute>
            <div>Dashboard Content</div>
          </ProtectedRoute>
        }
      />
      <Route path="/" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  )
}

function App() {
  return (
    <AuthProvider>
      <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">

      <Router>
        <AppRoutes />
      </Router>
      </ThemeProvider>
    </AuthProvider>
  )
}

export default App
