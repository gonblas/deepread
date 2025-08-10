import * as React from "react"
import { BookOpen, Brain, Home, Trophy, Settings, BarChart3, Plus } from "lucide-react"


import { NavMain } from "@/components/sidebar/NavMain"
import { NavUser } from "@/components/sidebar/NavUser"
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarHeader,
  SidebarRail,
} from "@/components/ui/sidebar"
import { useAuth } from "@/contexts/authContext"
import DeepReadHorizontalIcon from "../icons/DeepReadhorizontalIcon"

const data = {
  navMain: [
    {
      title: "Principal",
      items: [
        {
          title: "Dashboard",
          url: "/",
          icon: Home,
          isActive: true,
        },
        {
          title: "Explorar Libros",
          url: "/books",
          icon: BookOpen,
        },
        {
          title: "My Quizzes",
          url: "/my-quizzes",
          icon: Brain,
        },
      ],
    },
    {
      title: "Statistics",
      items: [
        {
          title: "Ranking",
          url: "/leaderboard",
          icon: Trophy,
        },
        {
          title: "Analytics",
          url: "/analytics",
          icon: BarChart3,
        },
        {
          title: "Explorar Libros",
          collapsible: true,
          icon: BookOpen,
          items: [
            { title: "Libro 1", url: "/books/1" },
            { title: "Libro 2", url: "/books/2" },
          ],
        },
      ],
    },
    {
      title: "Gestión",
      items: [
        {
          title: "Crear Quiz",
          url: "/create",
          icon: Plus,
        },
        {
          title: "Configuración",
          url: "/settings",
          icon: Settings,
        },
      ],
    },
  ],
}


export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {

  const { user } = useAuth()
  return (
    <Sidebar collapsible="icon" {...props}>
      <SidebarHeader className="flex items-center justify-between">
        <DeepReadHorizontalIcon className="text-sidebar w-3/5 h-auto"/>
      </SidebarHeader>
      <SidebarContent>
        <NavMain items={data.navMain} />
      </SidebarContent>
      <SidebarFooter>
        { user && <NavUser user={user} /> }
      </SidebarFooter>
      <SidebarRail />
    </Sidebar>
  )
}
