import * as React from "react"


import { NavMain } from "@/components/sidebar/NavMain"
import { NavUser } from "@/components/sidebar/NavUser"
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarHeader,
  SidebarRail
} from "@/components/ui/sidebar"
import { useAuth } from "@/contexts/authContext"
import DeepReadHorizontalIcon from "../icons/DeepReadhorizontalIcon"
interface AppSidebarProps {
  props?: React.ComponentProps<typeof Sidebar>
}

export function AppSidebar({  ...props }: AppSidebarProps) {

  const { user } = useAuth()
  return (
    <Sidebar {...props}>
      <SidebarHeader className="flex items-center justify-between">
        <DeepReadHorizontalIcon className="text-sidebar w-9/12 h-auto mt-2"/>
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
