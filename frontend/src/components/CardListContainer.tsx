export function CardListContainer({
  children,
  empty = false,
  loading = false,
  error = false,
}: {
  children: React.ReactNode;
  empty?: boolean;
  loading?: boolean;
  error?: boolean;
}) {
  if(empty || loading || error) return <></>
  return (
    
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      {children}
    </div>
  );
}
