package Simulation;

import javafx.util.Pair;

import java.util.*;

public class Delaunay {
    private static int AIMS = 450;
    private static double THOLD = Math.PI / 500;
    private static boolean convexBuilt = false;
    private static double EPS = 1e-5;
    private static Vect[] points;
    private static int n;
    private static ArrayList<Face> faces;
    Delaunay() {
        usedPoints = new HashMap<>();
    }


    private static class Edge {
        Vect begin;
        Vect end;
        Face face;
        Edge bro;
        Edge(Vect begin, Vect end, Face face) {
            this.begin = begin;
            this.end = end;
            this.face = face;
        }
        Vect getVect() {
            return (this.end.sub(this.begin));
        }
        boolean isBro(Edge b)  {
            return (this.begin == b.end && this.end == b.begin);
        }
    }
    private static class Face {
        Face(Vect x, Vect y, Vect z, int id) {
            this.id = id;
            vects = new Vect[]{x, y, z};
            edges = new Edge[3];
            for (int i = 0; i < 3; i++) {
                edges[i] = new Edge(vects[i], vects[(i + 1) % 3], this);
            }
        }
        Edge[] edges;
        Vect[] vects;
        int id;
        void connect(Face a) {
            for (Edge v : edges) {
                for (Edge u : a.edges) {
                    if (v.isBro(u)) {
                        v.bro = u;
                        u.bro = v;
                    }
                }
            }
        }
        double[] eq() {
            Vect n = vects[1].sub(vects[0]).mulVect(vects[2].sub(vects[0]));
            return new double[]{n.x, n.y, n.z, -n.x * vects[0].x - n.y * vects[0].y - n.z * vects[0].z};
        }
    }
    private static Pair<HashSet<Integer>, Integer> checkFaces(Vect x) {
        HashSet<Integer> ans = new HashSet<>();
        int last = -1;
        for (int i = 0; i < faces.size(); i++) {
            Vect v = faces.get(i).edges[0].getVect().mulVect(faces.get(i).edges[1].getVect());
            double angle = v.angBetw(x.sub(faces.get(i).edges[0].begin));
            if (angle > Math.PI / 2) {
                ans.add(i);
                last = i;
            }
        }
        return new Pair<>(ans, last);
    }
    private static void connectNewFace(int pointer, Edge edge, Vect x) {
        if (pointer >= faces.size()) {
            faces.add(new Face(edge.begin, edge.end, x, faces.size()));
            faces.get(faces.size() - 1).connect(edge.bro.face);
        } else {
            faces.set(pointer, new Face(edge.begin, edge.end, x, pointer));
            faces.get(pointer).connect(edge.bro.face);
        }

        //KOPYPASTA!!!!
    }

    private static boolean[] used;
    private static ArrayList<Edge> horizon;

    private static void dfs(int node, HashSet<Integer> visible) {
        used[node] = true;
        for (Edge edge : faces.get(node).edges) {
            if (visible.contains(edge.bro.face.id) && !used[edge.bro.face.id]) {
                dfs(edge.bro.face.id, visible);
            }
            else if(!visible.contains(edge.bro.face.id)) {
                horizon.add(edge);
            }
        }

    }

    private static void addNewPoint(HashSet<Integer> visible, int first, Vect x) {
        used = new boolean[faces.size() + 2];
        horizon = new ArrayList<>();
        dfs(first, visible);
        Iterator<Integer> iter1 = visible.iterator(), iter2;
        int cnt = 0, t;
        while (iter1.hasNext()) {
            t = iter1.next();
            connectNewFace(t, horizon.get(cnt++), x);
        }
        for (int i = 0; i < 2; i++) {
            visible.add(faces.size());
            connectNewFace(AIMS * 3, horizon.get(cnt++), x);
        }
        iter1 = visible.iterator();
        while (iter1.hasNext()) {
            int t1 = iter1.next();
            iter2 = visible.iterator();
            while (iter2.hasNext()) {
                int t2 = iter2.next();
                faces.get(t1).connect(faces.get(t2));
            }
        }
    }

    synchronized static void buildConvex(Aim[] aims) {
        n = aims.length;
        points = new Vect[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Vect(aims[i].getX(), aims[i].getY(), aims[i].getZ());
        }
        if (convexBuilt)
            return;
        convexBuilt = true;
        faces = new ArrayList<>();
        Pair<HashSet<Integer>, Integer> a;
        faces.add(new Face(points[0], points[1], points[2], 0));
        faces.add(new Face(points[0], points[3], points[1], 1));
        faces.add(new Face(points[1], points[3], points[2], 2));
        faces.add(new Face(points[0], points[2], points[3], 3));
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++)
                faces.get(i).connect(faces.get(j));
        }
        for (int i = 4; i < n; i++) {
            a = checkFaces(points[i]);
            addNewPoint(a.getKey(), a.getValue(), points[i]);
        }
    }

    private Face[] currFace = new Face[]{null, null};
    private Map<Vect, Double> usedPoints;

    private static double sqr(Vect x, Vect y, Vect z) {
        double a = x.sub(y).length(), b = x.sub(z).length(), c = y.sub(z).length();
        double p = (a + b + c) / 2;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

    private static boolean checkFace(Vect s, Face f) {
        double[] eq = f.eq();
        double t = (-eq[3]) / (eq[0] * s.x + eq[1] * s.y + eq[2] * s.z);
        s = s.mulA(t);
        return Math.abs(sqr(f.vects[0], f.vects[1], f.vects[2]) - sqr(f.vects[0], f.vects[1], s) - sqr(f.vects[2], f.vects[1], s) - sqr(f.vects[0], f.vects[2], s)) < EPS;
    }

    private static Vect pointByPoints(Vect a, Vect b, Vect c) {
        Vect mul = (b.sub(a)).mulVect(c.sub(a));
        mul = mul.mulA(1 / mul.length());
        return mul;
    }

    private Face findFace(Vect x, boolean q) {
        for (Face f : faces) {
            if (q && currFace[0] == f)
                continue;
            if (checkFace(x, f))
                return f;
        }
        return faces.get(0); // kostil
    }

    private Face bfs(Face curr, Vect x, int i) {
        if(checkFace(x, curr))
            return curr;
        Set<Face> s = new HashSet<>();
        Queue<Face> q = new LinkedList<>();
        q.add(curr);
        while (!q.isEmpty()) {
            curr = q.poll();
            s.add(curr);
            if (checkFace(x, curr) && curr != currFace[i])
                return curr;
            for (Edge e : curr.edges) {
                if (!s.contains(e.bro.face))
                    q.add(e.bro.face);
            }
        }
        return curr; // kostil
    }

    int nearestFace(Sputnik a, Sputnik b, Sputnik c) {
        Vect point = pointByPoints(a.getY(), b.getY(), c.getY());
        if (currFace[0] == null) {
            for (int i = 0; i < 2; i++) {
                currFace[i] = findFace(point, i == 1);
            }
        }
        Vect mul = (b.getY().sub(a.getY())).mulVect(c.getY().sub(a.getY()));
        for (int i = 0; i < 2; i++)
            currFace[i] = bfs(currFace[i], point, (i + 1) % 2);
        for (Face face : currFace) {
            for (Vect v : face.vects) {
                if (Math.abs(v.angBetw(mul)) < THOLD
                        || Math.abs(v.angBetw(mul.mulA(-1))) < THOLD) {
                    usedPoints.put(v, 1.);
//                    return new Pair<>(1, v);
            return 1;
                }
            }
        }

//        return new Pair<>(-1, null);
        return -1;
    }
}
