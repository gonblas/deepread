import { Button } from "@/components/ui/button"
import { AppSidebar } from "@/components/sidebar/AppSidebar"
import {
  SidebarInset,
  SidebarProvider,
  SidebarTrigger,
} from "@/components/ui/sidebar"

function HomePage() {
  return (
    <SidebarProvider>
      <AppSidebar />
      <SidebarInset>
        <div className="flex min-h-svh flex-col items-center justify-center">
          <Button>Click me</Button>
          <SidebarTrigger className="-ml-1" />
        </div>
      </SidebarInset>
    </SidebarProvider>
  )
}

export default HomePage