import AppLayout from "@/components/AppLayout";
import { SectionCards } from "@/components/dashboard/SectionCards";
import { BarChartInteractive } from "@/components/dashboard/BarChartInteractive";
import { RecentAttemptsTable } from "@/components/dashboard/RecentAttemptsTable";
import { useStatistics } from "@/contexts/statisticsContext";

function HomePage() {
  const { stats, error } = useStatistics();

  if (error) return <div>Error: {error}</div>;

  return (
    <AppLayout>
        <div className="flex flex-1 flex-col">
          <div className="@container/main flex flex-1 flex-col gap-2">
            <div className="flex flex-col gap-4 py-4 md:gap-6 md:py-6 px-4">
              <SectionCards stats={stats} />
              <BarChartInteractive data={stats.dailyStatsTimeline} />
              <RecentAttemptsTable />
            </div>
          </div>
        </div>
    </AppLayout>
  );
}

export default HomePage;
