import React, { createContext, useContext, useEffect, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import { LucideIcon } from "lucide-react";
import {
  defaultSidebar,
  booksSidebar,
  selectedBookSidebar,
} from "@/config/sidebar.config";
import { Chapter } from "./chapterContext";
import Cookies from "js-cookie";

export interface SidebarItem {
  title: string;
  url?: string;
  icon?: LucideIcon;
  isActive?: boolean;
  collapsible?: boolean;
  items?: SidebarItem[];
}

export interface SidebarSection {
  title: string;
  collapsible?: boolean;
  items: SidebarItem[];
}

export type SidebarItemsData = SidebarSection[];

interface SidebarContextType {
  sidebarItems: SidebarItemsData;
  setSidebarItems: React.Dispatch<React.SetStateAction<SidebarItemsData>>;
}

const SidebarContext = createContext<SidebarContextType | undefined>(undefined);

export function SidebarProvider({ children }: { children: React.ReactNode }) {
  const location = useLocation();
  const { bookId, chapterId } = useParams();
  const [sidebarItems, setSidebarItems] =
    useState<SidebarItemsData>(defaultSidebar);

  const [chaptersSidebarItems, setChaptersSidebarItems] = useState<
    { id: string; number: number; title: string }[]
  >([]);

  useEffect(() => {
    if(bookId === undefined) {setChaptersSidebarItems([]); return;}
    fetch(`http://localhost:8080/api/books/${bookId}/chapters`, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${Cookies.get("token")}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setChaptersSidebarItems(
          data.content.map((chapter: Chapter) => ({
            id: chapter.id,
            number: chapter.number,
            title: chapter.title,
          }))
        );
      })
      .catch((error) => {
        console.error("Error fetching chapters:", error);
      });
  }, [bookId]);

  useEffect(() => {
    let config: SidebarItemsData;

    if (bookId) {
      config = selectedBookSidebar(bookId, chaptersSidebarItems, chapterId);
    } else if (location.pathname.startsWith("/books")) {
      config = booksSidebar;
    } else {
      config = defaultSidebar;
    }

    config = config.map((section) => ({
      ...section,
      items: section.items.map((item) => ({
        ...item,
        isActive: item.url === location.pathname,
      })),
    }));

    setSidebarItems(config);
  }, [location.pathname, bookId]);

  return (
    <SidebarContext.Provider
      value={{
        sidebarItems,
        setSidebarItems,
      }}
    >
      {children}
    </SidebarContext.Provider>
  );
}

export function useCustomSidebar() {
  const context = useContext(SidebarContext);
  if (!context)
    throw new Error("useSidebar must be used within a SidebarProvider");
  return context;
}
