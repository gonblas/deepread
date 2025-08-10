import AppLayout from "@/components/AppLayout";
import { SectionCards } from "@/components/dashboard/SectionCards";
import { BarChartInteractive } from "@/components/dashboard/BarChartInteractive";
import { RecentAttemptsTable } from "@/components/dashboard/RecentAttemptsTable";
import { useStatistics } from "@/contexts/statisticsContext";
import { useEffect } from "react";
import { useAuth } from "@/contexts/authContext";

function HomePage() {
  const { fetchUserStats, stats, dailyStatsTimeline, loading, error } =
    useStatistics();
  const { user } = useAuth();

  useEffect(() => {
    if (user) {
      fetchUserStats();
    }
  }, [user]);

  return (
    <AppLayout>
      <div className="flex flex-1 flex-col">
        <div className="@container/main flex flex-1 flex-col gap-2">
          {loading && <div className="p-4">Loading statistics...</div>}
          {error && <div className="p-4 text-red-500">{error}</div>}
          {!loading && !error && (
            <div className="flex flex-col gap-4 py-4 md:gap-6 md:py-6 px-4">
              <SectionCards stats={stats} />
              <BarChartInteractive chartData={dailyStatsTimeline} />
              <RecentAttemptsTable />
            </div>
          )}
        </div>
      </div>
    </AppLayout>
  );
}

export default HomePage;
