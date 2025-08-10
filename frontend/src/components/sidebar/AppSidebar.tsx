import * as React from "react"


import { NavMain, SidebarItemsData } from "@/components/sidebar/NavMain"
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

interface AppSidebarProps {
  props?: React.ComponentProps<typeof Sidebar>
}

export function AppSidebar({  ...props }: AppSidebarProps) {

  const { user } = useAuth()
  return (
    <Sidebar collapsible="icon" {...props}>
      <SidebarHeader className="flex items-center justify-between">
        <DeepReadHorizontalIcon className="text-sidebar w-3/5 h-auto"/>
      </SidebarHeader>
      <SidebarContent>
        <NavMain />
      </SidebarContent>
      <SidebarFooter>
        { user && <NavUser user={user} /> }
      </SidebarFooter>
      <SidebarRail />
    </Sidebar>
  )
}
