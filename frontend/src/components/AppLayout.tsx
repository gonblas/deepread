import { AppSidebar } from "@/components/sidebar/AppSidebar";
import {
  SidebarInset,
  SidebarProvider,
  SidebarTrigger,
} from "@/components/ui/sidebar";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { Separator } from "@/components/ui/separator";
import { useLocation, Link } from "react-router-dom";

interface AppLayoutProps {
  children?: React.ReactNode;
}

function AppLayout({ children }: AppLayoutProps) {
  const location = useLocation();

  const pathParts = location.pathname.split("/").filter(Boolean);

  function formatBreadcrumbPart(part: string) {
    return part
      .split("-")
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
      .join(" ");
  }

  const breadcrumbItems = pathParts.flatMap((part, index) => {
    const href = "/" + pathParts.slice(0, index + 1).join("/");
    const isLast = index === pathParts.length - 1;
    const label = formatBreadcrumbPart(decodeURIComponent(part));

    const item = (
      <BreadcrumbItem key={href}>
        {isLast ? (
          <BreadcrumbPage>{label}</BreadcrumbPage>
        ) : (
          <BreadcrumbLink asChild>
            <Link to={href}>{label}</Link>
          </BreadcrumbLink>
        )}
      </BreadcrumbItem>
    );

    if (!isLast) {
      return [item, <BreadcrumbSeparator key={`sep-${href}`} />];
    }
    return [item];
  });

  return (
    <SidebarProvider>
      <AppSidebar />
      <SidebarInset>
        <header className="flex h-16 shrink-0 items-center gap-2 transition-[width,height] ease-linear group-has-data-[collapsible=icon]/sidebar-wrapper:h-12">
          <div className="flex items-center gap-2 px-4">
            <SidebarTrigger className="-ml-1" />
            <Separator
              orientation="vertical"
              className="mr-2 data-[orientation=vertical]:h-4"
            />
            <Breadcrumb>
              <BreadcrumbList>
                <BreadcrumbItem>
                  <BreadcrumbLink asChild>
                    <Link to="/Dashboard">Home</Link>
                  </BreadcrumbLink>
                </BreadcrumbItem>
                {pathParts.length > 0 && <BreadcrumbSeparator />}
                {breadcrumbItems}
              </BreadcrumbList>
            </Breadcrumb>
          </div>
        </header>
        <main className="flex flex-col gap-4 py-4 md:gap-6 md:py-6 px-6 min-h-svh">
          {children}
        </main>
      </SidebarInset>
    </SidebarProvider>
  );
}

export default AppLayout;
