import React, { createContext, useContext, useEffect, useState } from "react";
import type { LucideIcon } from "lucide-react";
import { useLocation } from "react-router-dom";

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

export function SidebarProvider({
  children,
  initialItems = [],
}: {
  children: React.ReactNode;
  initialItems?: SidebarItemsData;
}) {
  const [sidebarItems, setSidebarItems] = useState<SidebarItemsData>(initialItems);
  const location = useLocation();
  
  useEffect(() => {
    setSidebarItems((prev) =>
      prev.map(section => ({
        ...section,
        items: section.items.map(item => ({
          ...item,
          isActive: item.url === location.pathname
        }))
      }))
    );
  }, [location.pathname, setSidebarItems]);

  return (
    <SidebarContext.Provider value={{ sidebarItems, setSidebarItems }}>
      {children}
    </SidebarContext.Provider>
  );
}

export function useSidebar() {
  const context = useContext(SidebarContext);
  if (!context) throw new Error("useSidebar must be used within a SidebarProvider");
  return context;
}