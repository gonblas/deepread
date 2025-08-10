import React, { createContext, useContext, useState, useEffect } from "react";
import Cookies from "js-cookie";

interface User {
  email: string;
  username: string;
}

interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => Promise<boolean>;
  signup: (
    email: string,
    password: string,
    username: string
  ) => Promise<boolean>;
  logout: () => void;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const savedUser = localStorage.getItem("user");
    if (savedUser) {
      const parsedUser = JSON.parse(savedUser);
      setUser(parsedUser);
    }
    setIsLoading(false);
  }, []);

  const saveUser = (email: string, username: string, token: string) => {
    const newUser = { email, username };
    setUser(newUser);
    localStorage.setItem("user", JSON.stringify(newUser));
    Cookies.set("token", token, {
      expires: 7,
      path: "/",
    });
  };

  const login = async (email: string, password: string): Promise<boolean> => {
    setIsLoading(true);

    fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password }),
    })
      .then((response) => response.json())
      .then((data) => {
        saveUser(data.email, data.username, data.token);
      })
      .catch((error) => {
        console.error("Error:", error);
      });

    setIsLoading(false);
    return true;
  };

  const signup = async (
    email: string,
    password: string,
    username: string
  ): Promise<boolean> => {
    setIsLoading(true);

    fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password, username }),
    })
      .then((response) => response.json())
      .then((data) => {
        saveUser(data.email, data.username, data.token);
      })
      .catch((error) => {
        console.error("Error:", error);
      });

    setIsLoading(false);
    return true;
  };

  const logout = () => {
    setUser(null);
    Cookies.remove("token", { path: "/" });
    localStorage.removeItem("user");
  };

  return (
    <AuthContext.Provider value={{ user, login, signup, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
