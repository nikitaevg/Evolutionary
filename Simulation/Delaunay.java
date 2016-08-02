package Simulation;

import javafx.util.Pair;

import java.util.*;

public class Delaunay { // first part creates Delaunay triangulation. The second part finds nearest point
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
            return new double[]{n.getX(), n.getY(), n.getZ(), -n.getX() * vects[0].getX()
                    - n.getY() * vects[0].getY() - n.getZ() * vects[0].getZ()};
        }
    }
    private static Pair<HashSet<Integer>, Integer> checkFaces(Vect x) {
        HashSet<Integer> ans = new HashSet<>();
        int last = -1;
        for (int i = 0; i < faces.size(); i++) {
            Vect v = faces.get(i).edges[0].getVect().mulVect(faces.get(i).edges[1].getVect());
            double angle = v.angBetween(x.sub(faces.get(i).edges[0].begin));
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

    //---------------------------------------------------------------------------

    private static class Observe { // class to remember previous shots and to calculate P
        private static double HEIGHTCONSTRAINT = 1e7;
        private ArrayList<Double> heights;
        private ArrayList<Double> buff;
        Observe() {
            heights = new ArrayList<>();
            buff = new ArrayList<>();
        }
        private boolean add(double h) {
            heights.add(h);
            Collections.sort(this.heights);
            for (int i = 0; i < heights.size() - 1; i++) {
                if (heights.get(i + 1) / heights.get(i) < 3)
                    return false;
            }
            return true;
        }
        private int innerAdd(double h) {
            if (h < HEIGHTCONSTRAINT)
                return 0;

            switch (heights.size()){
                case 3:
                    return 0;
                case 0:
                    heights.add(h);
                    return 1;
                case 1:
                    if (add(h))
                        return 3;
                    else
                        return 1;
                case 2:
                    if (add(h))
                        return 6;
                    else if(heights.get(2) / heights.get(0) >= 3)
                        return 3;
                    else return 1;
            }
            return 0;
        }
        int addH(double h, boolean toWrite) {
            if (heights.size() > 0) {
                int a = 0;
            }
            buff = (ArrayList<Double>) heights.clone();
            int ans = innerAdd(h);
            if (!toWrite)
                heights = (ArrayList<Double>) buff.clone();
            return ans;
        }
    }

    private Face[] currFace = new Face[]{null, null}; // two nearest faces
    private Map<Vect, Observe> usedPoints;
    private Set<Face> s = new HashSet<>();
    private Queue<Face> q = new LinkedList<>();

    private static double square(Vect x, Vect y, Vect z) { //square of the triangle
        double a = x.sub(y).length(), b = x.sub(z).length(), c = y.sub(z).length();
        double p = (a + b + c) / 2;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

    private static boolean checkFace(Vect s, Face f) { //whether s belongs to face f
        double[] eq = f.eq();
        double t = (-eq[3]) / (eq[0] * s.getX() + eq[1] * s.getY() + eq[2] * s.getZ());
        s.mulA(t);
        return Math.abs(square(f.vects[0], f.vects[1], f.vects[2]) - square(f.vects[0], f.vects[1], s)
                - square(f.vects[2], f.vects[1], s) - square(f.vects[0], f.vects[2], s)) < EPS;
    }

    private static Vect pointBySurface(Vect a, Vect b, Vect c) { // finds point on the sphere perpendicular to face
        Vect mul = (b.sub(a)).mulVect(c.sub(a));
        mul.mulA(1 / mul.length());
        return mul;
    }

    private Face findFace(Vect x, boolean q) { // linear search of faces
        for (Face f : faces) {
            if (q && currFace[0] == f)
                continue;
            if (checkFace(x, f))
                return f;
        }
        return faces.get(0);
    }

    private Face bfs(Face curr, Vect x, int i) {

        if(checkFace(x, curr))
            return curr;
        s.clear();
        q.clear();
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
        return curr;
    }



    private double fitness(Vect a, Vect b, Vect c, Vect x, Vect mul, boolean toWrite) {
        double la = a.sub(b).length();
        double lb = a.sub(c).length();
        double lc = b.sub(c).length();
        double p = (la + lb + lc) / 2;
        double sq = Math.sqrt(p * (p - la) * (p - lb) * (p - lc));
        double minH = (2 * sq / Math.max(Math.max(la, lb), lc));
        if (!usedPoints.containsKey(x)) {
            usedPoints.put(x, new Observe());
        }
        int P = usedPoints.get(x).addH(minH, toWrite);
        return P * minH * (0.2 + Math.pow(Math.cos(x.angBetweenLines(mul)), 2)) / 1e3;
    }

    double nearestFace(Sputnik a, Sputnik b, Sputnik c) {
        Vect point = pointBySurface(a.getY(), b.getY(), c.getY());
        if (currFace[0] == null) {
            for (int i = 0; i < 2; i++) {
                currFace[i] = findFace(point, i == 1);
            }
        }
        Vect mul = (b.getY().sub(a.getY())).mulVect(c.getY().sub(a.getY()));
        for (int i = 0; i < 2; i++)
            currFace[i] = bfs(currFace[i], point, (i + 1) % 2);
        double maxF = 0;
        double t;
        Vect bestV = currFace[0].vects[0];
        for (Face face : currFace) {
            for (Vect v : face.vects) {
                t = fitness(a.getY(), b.getY(), c.getY(), v, mul, false);
                if (t > maxF) {
                    maxF = t;
                    bestV = v;
                }
//                if (v.angBetweenLines(mul) < THOLD) {
//                    return new Pair<>(1, v);
//                    return 1;
//                }
            }
        }
        return fitness(a.getY(), b.getY(), c.getY(), bestV, mul, true);
//        return new Pair<>(-1, null);
//        return -1;
    }
}
