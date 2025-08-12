import React, { createContext, useContext, useEffect, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import { LucideIcon } from "lucide-react";
import { defaultSidebar, booksSidebar, selectedBookSidebar } from "@/config/sidebar.config";

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
  setChaptersSidebarItems: React.Dispatch<React.SetStateAction<{ id: string; number: number; title: string }[]>>;
}

const SidebarContext = createContext<SidebarContextType | undefined>(undefined);

export function SidebarProvider({ children }: { children: React.ReactNode }) {
  const location = useLocation();
  const params = useParams();
  const [sidebarItems, setSidebarItems] = useState<SidebarItemsData>(defaultSidebar);
  const [chaptersSidebarItems, setChaptersSidebarItems] = useState<{ id: string; number: number; title: string }[]>([]);

  useEffect(() => {
    let config: SidebarItemsData;

    if (params.bookId) {
      config = selectedBookSidebar(params.bookId, chaptersSidebarItems);
    } else if (location.pathname.startsWith("/books")) {
      config = booksSidebar;
    } else {
      config = defaultSidebar;
    }

    config = config.map(section => ({
      ...section,
      items: section.items.map(item => ({
        ...item,
        isActive: item.url === location.pathname,
      })),
    }));

    setSidebarItems(config);
  }, [location.pathname, params.bookId, chaptersSidebarItems]);

  return (
    <SidebarContext.Provider value={{ sidebarItems, setSidebarItems, setChaptersSidebarItems }}>
      {children}
    </SidebarContext.Provider>
  );
}

export function useSidebar() {
  const context = useContext(SidebarContext);
  if (!context) throw new Error("useSidebar must be used within a SidebarProvider");
  return context;
}
