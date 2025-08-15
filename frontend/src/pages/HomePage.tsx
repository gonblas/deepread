import { SectionCards } from "@/components/statistics/SectionCards";
import { BarChartInteractive } from "@/components/statistics/BarChartInteractive";
import { RecentAttemptsTable } from "@/components/statistics/RecentAttemptsTable";
import { useStatistics } from "@/contexts/statisticsContext";
import { useEffect } from "react"; 
import { useAuth } from "@/contexts/authContext";
import { SectionHeader } from "@/components/SectionHeader";

const recentAttempts = [
  {
    id: "1",
    bookTitle: "One Hundred Years of Solitude",
    chapterNumber: 1,
    score: 85,
    date: "2025-08-09T09:00:00"
  },
  {
    id: "2",
    bookTitle: "Don Quixote",
    chapterNumber: 2,
    score: 72,
    date: "2025-08-09T07:00:00"
  },
  {
    id: "3",
    bookTitle: "Hopscotch",
    chapterNumber: 3,
    score: 45,
    date: "2025-08-08T10:00:00"
  },
  {
    id: "4",
    bookTitle: "The House of the Spirits",
    chapterNumber: 4,
    score: 88,
    date: "2025-08-08T09:00:00"
  },
  {
    id: "5",
    bookTitle: "Love in the Time of Cholera",
    chapterNumber: 2,
    score: 91,
    date: "2025-08-07T10:00:00"
  },
]

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
          <RecentAttemptsTable recentAttempts={recentAttempts} />
        </>
      )}
    </>
  );
}

export default HomePage;
