import { SectionCards } from "@/components/statistics/SectionCards";
import { BarChartInteractive } from "@/components/statistics/BarChartInteractive";
import { RecentAttemptsTable } from "@/components/statistics/RecentAttemptsTable";
import { useStatistics } from "@/contexts/statisticsContext";
import { useEffect } from "react"; 
import { useAuth } from "@/contexts/authContext";
import { SectionHeader } from "@/components/SectionHeader";

function HomePage() {
  const { fetchUserStats, userStats, loading, error } =
    useStatistics();
  const { user } = useAuth();

  useEffect(() => {
    if (user) {
      fetchUserStats();
    }
  }, [user]);

  return (
    <>
      <SectionHeader
        title="Dashboard"
        loading={loading}
        error={error}
        description="Overview of your book statistics and recent activity"
      />
      {loading && <div className="p-4">Loading statistics...</div>}
      {error && <div className="p-4 text-red-500">{error}</div>}
      {!loading && !error && (
        <>
          <SectionCards stats={userStats.stats} />
          <BarChartInteractive chartData={userStats.dailyStatsTimeline} />
          <RecentAttemptsTable />
        </>
      )}
    </>
  );
}

export default HomePage;
