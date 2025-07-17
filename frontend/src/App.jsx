import { Suspense, lazy } from "react"
import { Route, Routes, Outlet } from "react-router-dom"

const HomePage = lazy(() => import("./pages/HomePage"))

function Layout() {
  return (
    <>
      <Navbar />
      <Suspense
        fallback={
          <div className="flex items-center justify-center w-full h-screen">
            <div className="loader"></div>
          </div>
        }
      >
        <Outlet />
      </Suspense>
      <Footer />
    </>
  )
}

function NotFoundLayout() {
  return (
    <>
      <Navbar />
      <Suspense fallback={<div className="loader"></div>}>
        <NotFoundPage />
      </Suspense>
    </>
  )
}

const App = () => {
  return (
    <div
      className={`react-scroll bg-background w-full min-h-screen overflow-hidden text-white flex flex-col`}
    >
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<HomePage />} />
        </Route>
      </Routes>
    </div>
  )
}

export default App
