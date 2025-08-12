import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import { ThemeProvider } from "@/contexts/themeContext";
import { AuthProvider, useAuth } from "@/contexts/authContext";
import LoginPage from "@/pages/LoginPage";
import SignUpPage from "@/pages/SignUpPage";
import HomePage from "@/pages/HomePage";
import { Loader2 } from "lucide-react";
import { StatisticsProvider } from "./contexts/statisticsContext";
import { Toaster } from "sonner";
import { SidebarProvider } from "./contexts/sidebarContext";

import BookPage from "./pages/BookPage";
import CreateBookPage from "./pages/CreateBookPage";
import BookListPage from "./pages/BookListPage";
import { NotificationProvider } from "./contexts/notificationContext";
import { DataRefreshProvider } from "./contexts/dataRefreshContext";

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { user, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-ctp-base">
        <div className="text-center space-y-4">
          <Loader2 className="h-8 w-8 animate-spin text-ctp-mauve mx-auto" />
          <p className="text-ctp-subtext1">Cargando...</p>
        </div>
      </div>
    );
  }

  return user ? (
    <SidebarProvider>{children}</SidebarProvider>
  ) : (
    <Navigate to="/auth/login" replace />
  );
}

function PublicRoute({ children }: { children: React.ReactNode }) {
  const { user, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-ctp-base">
        <div className="text-center space-y-4">
          <Loader2 className="h-8 w-8 animate-spin text-ctp-mauve mx-auto" />
          <p className="text-ctp-subtext1">Cargando...</p>
        </div>
      </div>
    );
  }

  return user ? <Navigate to="/" replace /> : <>{children}</>;
}

function AppRoutes() {
  return (
    <Routes>
      <Route
        path="/auth/login"
        element={
          <PublicRoute>
            <LoginPage />
          </PublicRoute>
        }
      />
      <Route
        path="/auth/register"
        element={
          <PublicRoute>
            <SignUpPage />
          </PublicRoute>
        }
      />
      <Route
        path="/"
        element={
          <ProtectedRoute>
            <StatisticsProvider>
              <HomePage />
            </StatisticsProvider>
          </ProtectedRoute>
        }
      />
      <Route
        path="/books"
        element={
          <ProtectedRoute>
            <BookListPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/books/:bookId"
        element={
          <ProtectedRoute>
            <BookPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/books/create"
        element={
          <ProtectedRoute>
            <CreateBookPage />
          </ProtectedRoute>
        }
      />
      <Route path="/" element={<Navigate to="/Dashboard" replace />} />
    </Routes>
  );
}

function App() {
  return (
    <AuthProvider>
      <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
        <NotificationProvider>
          <Toaster />
          <Router>
            <DataRefreshProvider>
              <AppRoutes />
            </DataRefreshProvider>
          </Router>
        </NotificationProvider>
      </ThemeProvider>
    </AuthProvider>
  );
}

export default App;
